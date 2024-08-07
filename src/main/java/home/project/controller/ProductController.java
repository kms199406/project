package home.project.controller;


import home.project.domain.*;
import home.project.dto.ProductDTOWithoutId;
import home.project.response.CustomListResponseEntity;
import home.project.response.CustomOptionalResponseEntity;
import home.project.service.ProductService;
import home.project.util.ValidationCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Tag(name = "상품", description = "상품관련 API입니다")
@RequestMapping(path = "/api/product")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(schema = @Schema(ref = "#/components/schemas/ProductValidationFailedResponseSchema"))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(ref = "#/components/schemas/UnauthorizedResponseSchema"))),
        @ApiResponse(responseCode = "403", description = "Forbidden",
                content = @Content(schema = @Schema(ref = "#/components/schemas/ForbiddenResponseSchema"))),
        @ApiResponse(responseCode = "404", description = "Resource not found",
                content = @Content(schema = @Schema(ref = "#/components/schemas/NotFoundResponseSchema"))),
        @ApiResponse(responseCode = "409", description = "Conflict",
                content = @Content(schema = @Schema(ref = "#/components/schemas/ConflictResponseSchema"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(ref = "#/components/schemas/InternalServerErrorResponseSchema")))
})
@RestController
public class ProductController {

    private final ProductService productService;
    private final ValidationCheck validationCheck;

    @Autowired
    public ProductController(ProductService productService, ValidationCheck validationCheck) {
        this.productService = productService;
        this.validationCheck = validationCheck;
    }


    @Operation(summary = "상품 등록 메서드", description = "상품 등록 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/GeneralSuccessResponseSchema")))
    })
    @Transactional
    @PostMapping("create")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER')")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTOWithoutId productDTOWithoutId, BindingResult bindingResult) {
        try{
        CustomOptionalResponseEntity<?> validationResponse = validationCheck.validationChecks(bindingResult);
            Long currentStock = productDTOWithoutId.getStock();
            if (currentStock < 0) {
                throw new DataIntegrityViolationException("재고가 음수일 수 없습니다.");
            }
        if (validationResponse != null) return validationResponse;
        Product product = new Product();
        product.setBrand(productDTOWithoutId.getBrand());
        product.setCategory(productDTOWithoutId.getCategory());
        product.setSoldQuantity(productDTOWithoutId.getSoldQuantity());
        product.setName(productDTOWithoutId.getName());
        product.setStock(productDTOWithoutId.getStock());
        product.setImage(productDTOWithoutId.getImage());
        productService.join(product);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("successMessage", product.getName() + "(이)가 등록되었습니다.");
        return new CustomOptionalResponseEntity<>(Optional.of(responseMap), "상품 등록 성공", HttpStatus.OK);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "id로 상품 조회 메서드", description = "id로 상품 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProductResponseSchema")))
    })
    @GetMapping("product")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER', 'ROLE_USER')")
    public ResponseEntity<?> findProductById(@RequestParam("productId") Long productId) {
        try{
        if (productId == null) {
            throw new IllegalStateException("id가 입력되지 않았습니다.");
        }
        Optional<Product> productOptional = productService.findById(productId);
        String successMessage = productId + "에 해당하는 상품 입니다.";
        return new CustomOptionalResponseEntity<>(productOptional, successMessage, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "전체 상품 조회 메서드", description = "전체 상품 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/PagedProductListResponseSchema")))
    })
    @GetMapping("products")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER', 'ROLE_USER')")
    public ResponseEntity<?> findAllProduct(
            @PageableDefault(page = 1, size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) @ParameterObject Pageable pageable) {
        try{
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<Product> productList = productService.findAll(pageable);
        String successMessage = "전체 상품입니다.";
        long totalCount = productList.getTotalElements();
        int page = productList.getNumber();
        return new CustomListResponseEntity<>(productList.getContent(), successMessage, HttpStatus.OK, totalCount, page);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "상품 통합 조회 메서드", description = "브랜드명, 카테고리명, 상품명 및 일반 검색어로 상품을 조회합니다. 모든 조건을 만족하는 상품을 조회합니다. 검색어가 없으면 전체 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/PagedProductListResponseSchema")))
    })
    @GetMapping("search")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER', 'ROLE_USER')")
    public ResponseEntity<?> searchProducts(
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "content", required = false) String content,
            @PageableDefault(page = 1, size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "brand", direction = Sort.Direction.ASC)
            }) @ParameterObject Pageable pageable) {
        try{
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<Product> productPage = productService.findProducts(brand, category, productName, content, pageable);

            StringBuilder searchCriteria = new StringBuilder();
            if (brand != null) searchCriteria.append(brand).append(", ");
            if (category != null) searchCriteria.append(category).append(", ");
            if (productName != null) searchCriteria.append(productName).append(", ");
            if (content != null) searchCriteria.append(content).append(", ");

            String successMessage;
            if (!searchCriteria.isEmpty()) {
                searchCriteria.setLength(searchCriteria.length() - 2);
                successMessage = "검색 키워드 : " + searchCriteria;
            } else {
                successMessage = "전체 상품입니다.";
            }

        long totalCount = productPage.getTotalElements();
        int page = productPage.getNumber();
        return new CustomListResponseEntity<>(productPage.getContent(), successMessage, HttpStatus.OK, totalCount, page);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "전체 브랜드 조회 메서드", description = "브랜드 조회(판매량기준 오름차순정렬) 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/BrandListResponseSchema")))
    })
    @GetMapping("brands")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER', 'ROLE_USER')")
    public ResponseEntity<?> brandList(
            @PageableDefault(page = 1, size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "brand", direction = Sort.Direction.ASC)
            }) @ParameterObject Pageable pageable) {
        try{
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<Product> brandListPage = productService.brandList(pageable);
        String successMessage = "전체 브랜드 입니다.";
        long totalCount = brandListPage.getTotalElements();
        int page = brandListPage.getNumber();
        return new CustomListResponseEntity<>(brandListPage.getContent(), successMessage, HttpStatus.OK, totalCount, page);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @Operation(summary = "상품 업데이트(수정) 메서드", description = "상품 업데이트(수정) 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProductResponseSchema")))
    })
    @PutMapping("update")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER')")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid Product product, BindingResult bindingResult) {
        try{
        CustomOptionalResponseEntity<?> validationResponse = validationCheck.validationChecks(bindingResult);
        if (validationResponse != null) return validationResponse;
        Optional<Product> productOptional = productService.update(product);
        String successMessage = "상품 정보가 수정되었습니다.";
        return new CustomOptionalResponseEntity<>(productOptional, successMessage, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @Operation(summary = "상품 삭제 메서드", description = "상품 삭제 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/GeneralSuccessResponseSchema")))
    })
    @DeleteMapping("delete")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER')")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId) {
        try{
        String name = productService.findById(productId).get().getName();
        productService.deleteById(productId);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("successMessage", name +"(id:" +productId + ")(이)가 삭제되었습니다.");
        return new CustomOptionalResponseEntity<>(Optional.of(responseMap), "상품 삭제 성공", HttpStatus.OK);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @Operation(summary = "재고 수량 증가 메서드", description = "재고 수량 증가 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProductResponseSchema")))
    })
    @PutMapping("increase_stock")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER')")
    public ResponseEntity<?> increaseStock(@RequestParam("productId") Long productId, @RequestParam("stock") Long stock) {
        try{
        Product increaseProduct = productService.increaseStock(productId, stock);
        String successMessage = increaseProduct.getName() + "상품이 " + stock + "개 증가하여 " + increaseProduct.getStock() + "개가 되었습니다.";
        return new CustomOptionalResponseEntity<>(Optional.of(increaseProduct), successMessage, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @Operation(summary = "재고 수량 감소 메서드", description = "재고 수량 감소 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProductResponseSchema")))
    })
    @PutMapping("decrease_stock")
//    @SecurityRequirement(name = "bearerAuth")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CENTER')")
    public ResponseEntity<?> decreaseStock(@RequestParam("productId") Long productId, @RequestParam("stock") Long stock) {
        try{
        Product decreaseProduct = productService.decreaseStock(productId, stock);
        String successMessage = decreaseProduct.getName() + "상품이 " + stock + "개 감소하여 " + decreaseProduct.getStock() + "개가 되었습니다.";
        return new CustomOptionalResponseEntity<>(Optional.of(decreaseProduct), successMessage, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            String errorMessage = "접근 권한이 없습니다.";
            return new CustomOptionalResponseEntity<>(Optional.of(e.getMessage()), errorMessage, HttpStatus.FORBIDDEN);
        }
    }

}
