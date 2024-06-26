package home.project.controller;


//import home.project.domain.LoginDto;


import home.project.domain.*;
//import home.project.domain.TokenDto;
import home.project.exceptions.JwtAuthenticationException;
import home.project.exceptions.PageNotFoundException;
import home.project.service.JwtTokenProvider;
import home.project.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.models.PathItem;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "회원", description = "회원관련 API 입니다")
//@RequestMapping(path = "/api/member")
@RequestMapping(path = "/api/member")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Member.class))),
        @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = Member.class))),
        @ApiResponse(responseCode = "403", description = "접근이 금지되었습니다.", content = @Content(schema = @Schema(implementation = Member.class))),
        @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = Member.class)))
})

@RestController
public class MemberController {
    private final MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //    @Operation(summary = "로그인 메서드", description = "로그인 메서드입니다.")
//    @PostMapping("Login")
//    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            Map<String, String> errorMap = new HashMap<>();
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                errorMap.put(error.getField(), error.getDefaultMessage());
//            }
//            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
//        }
//        try {
//            memberService.login(loginDto);
//            return ResponseEntity.ok(loginDto);
//        } catch (DataIntegrityViolationException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }
    @Operation(summary = "회원가입 메서드", description = "회원가입 메서드입니다.")
    @PostMapping("Join")
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberDTOWithoutId memberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> responseMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                responseMap.put(error.getField(), error.getDefaultMessage());
            }
            CustomOptionalResponseBody<Optional<Member>> errorBody = new CustomOptionalResponseBody<>(Optional.ofNullable(responseMap), "Validation failed", HttpStatus.BAD_REQUEST.value());
            return new CustomOptionalResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }

        Member member = new Member();
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setName(memberDTO.getName());
        member.setPhone(memberDTO.getPhone());
        memberService.join(member);

        // 회원가입 후 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword()));

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("accessToken", tokenDto.getAccessToken());
        responseMap.put("refreshToken", tokenDto.getRefreshToken());
        responseMap.put("message", "회원가입이 성공적으로 완료되었습니다.");
        CustomOptionalResponseBody<Optional<Map<String, String>>> responseBody = new CustomOptionalResponseBody<>(Optional.of(responseMap), "회원가입 성공", HttpStatus.OK.value());
        return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "이메일로회원조회 메서드", description = "이메일로회원조회 메서드입니다.")
    @GetMapping("FindByEmail")
    public CustomOptionalResponseEntity<Optional<Member>> findMemberByEmail(@RequestParam("MemberEmail") @Valid String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalStateException("이메일이 입력되지 않았습니다.");
        }
        if (!email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$")) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        Optional<Member> memberOptional = memberService.findByEmail(email);
        String successMessage = email + "로 가입된 회원정보입니다";
        return new CustomOptionalResponseEntity<>(Optional.ofNullable(memberOptional), successMessage, HttpStatus.OK);
    }

    @Operation(summary = "ID로 회원조회 메서드", description = "ID로 회원조회 메서드입니다.")
    @GetMapping("findMemberById")
    public CustomOptionalResponseEntity<Optional<Member>> findMemberById(@RequestParam("MemberId") Long id) {
        if (id == null) {
            throw new IllegalStateException("id가 입력되지 않았습니다.");
        }
        Optional<Member> memberOptional = memberService.findById(id);
        String successMessage = id + "으로 가입된 회원정보입니다";
        return new CustomOptionalResponseEntity<>(Optional.ofNullable(memberOptional), successMessage, HttpStatus.OK);
    }

    @Operation(summary = "전체회원조회 메서드", description = "전체회원조회 메서드입니다.")
    @GetMapping("FindAllMember")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findAllMember(
            @PageableDefault(page = 1, size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) @ParameterObject Pageable pageable) {
        try {
            Page<Member> memberPage = memberService.findAll(pageable);
            Page<MemberDTOWithoutPw> memberDtoPage = memberPage.map(member ->
                    new MemberDTOWithoutPw(member.getId(), member.getEmail(), member.getName(), member.getPhone()));
            String successMessage = "전체 회원입니다";
            long totalCount = memberPage.getTotalElements();
            int page = memberPage.getNumber();
            return new CustomListResponseEntity<>(memberDtoPage.getContent(), successMessage, HttpStatus.OK, totalCount, page);
        } catch (AccessDeniedException e) {
            String errorMessage = "에 해당하는 상품 입니다";
            return new CustomOptionalResponseEntity<>(Optional.ofNullable(e.getMessage()), errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "회원 통합 조회 메서드", description = "이름, 이메일, 전화번호 및 일반 검색어로 회원을 조회합니다. 모든 조건을 만족하는 회원을 조회합니다. 검색어가 없으면 전체 회원을 조회합니다.")
    @GetMapping("/searchMembers")
    public ResponseEntity<?> searchMembers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "query", required = false) String query,
            @PageableDefault(page = 1, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            }) @ParameterObject Pageable pageable) {

        Page<Member> memberPage = memberService.findMembers(name, email, phone, query, pageable);
        String successMessage = "검색 결과입니다";

//        if (pageable.getPageNumber() >= memberPage.getTotalPages()) {
//            throw new PageNotFoundException("요청한 페이지가 존재하지 않습니다.");
//        }

        long totalCount = memberPage.getTotalElements();
        int page = memberPage.getNumber();

        return new CustomListResponseEntity<>(memberPage.getContent(), successMessage, HttpStatus.OK, totalCount, page);
    }



    @Operation(summary = "회원정보업데이트(수정) 메서드", description = "회원정보업데이트(수정) 메서드입니다.")
    @PutMapping("UpdateMember")
    public ResponseEntity<?> updateMember(@RequestBody @Valid Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> responseMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                responseMap.put(error.getField(), error.getDefaultMessage());
            }
            CustomOptionalResponseBody<Optional<Product>> errorBody = new CustomOptionalResponseBody<>(Optional.ofNullable(responseMap), "Validation failed", HttpStatus.BAD_REQUEST.value());
            return new CustomOptionalResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }
            Optional<Member> memberOptional = memberService.update(member);
            String successMessage = "정보가 수정되었습니다";
            return new CustomOptionalResponseEntity<>(Optional.ofNullable(memberOptional), successMessage, HttpStatus.OK);
    }


    @Transactional
    @Operation(summary = "멤버 삭제 메서드", description = "멤버를 삭제하는 메서드입니다.")
    @DeleteMapping("DeleteMember") // 멤버 삭제
    public ResponseEntity<?> deleteMember(@RequestParam("memberId") Long memberId) {

            memberService.deleteMember(memberId);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("이용해주셔서 감사합니다", memberId+"님의 계정이 삭제되었습니다");
            CustomOptionalResponseBody responseBody = new CustomOptionalResponseBody<>(Optional.ofNullable(responseMap),"회원삭제 성공", HttpStatus.OK.value());
            return new CustomOptionalResponseEntity<>(responseBody, HttpStatus.OK);
    }


}