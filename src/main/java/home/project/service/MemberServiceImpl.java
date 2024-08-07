package home.project.service;

import home.project.domain.Member;
import home.project.dto.MemberDTOWithoutId;
import home.project.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder/*, AuthenticationManagerBuilder authenticationManagerBuilder, OAuth2ResourceServerProperties.Jwt jwt*/) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member convertToEntity(MemberDTOWithoutId memberDTOWithoutId) {
        Member member = new Member();
        member.setEmail(memberDTOWithoutId.getEmail());
        member.setPassword(passwordEncoder.encode(memberDTOWithoutId.getPassword()));
        member.setName(memberDTOWithoutId.getName());
        member.setPhone(memberDTOWithoutId.getPhone());
        return member;
    }

    public void join(Member member) {
        boolean emailExists = memberRepository.existsByEmail(member.getEmail());
        boolean phoneExists = memberRepository.existsByPhone(member.getPhone());
        if (emailExists && phoneExists) {
            throw new DataIntegrityViolationException("이미 사용 중인 이메일과 휴대폰번호입니다.");
        } else if (emailExists) {
            throw new DataIntegrityViolationException("이미 사용 중인 이메일입니다.");
        } else if (phoneExists) {
            throw new DataIntegrityViolationException("이미 사용 중인 휴대폰번호입니다.");
        }
        memberRepository.save(member);
    }

    public Optional<Member> findById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new IllegalArgumentException(memberId + "(으)로 등록된 회원이 없습니다.");
        });
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> { throw new IllegalArgumentException(email+"(으)로 등록된 회원이 없습니다."); });
        return Optional.ofNullable(member);
    }

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Page<Member> findMembers(String name, String email, String phone, String role, String content, Pageable pageable) {
        Page<Member> memberPage = memberRepository.findMembers(name, email, phone, role, content, pageable);
        if (memberPage.getSize() == 0 || memberPage.getTotalElements() == 0) {
            throw new IllegalArgumentException("해당하는 회원이 없습니다.");
        }
        return memberPage;
    }

    public Optional<Member> update(Member member) {
        Member existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException(member.getId() + "(으)로 등록된 회원이 없습니다."));

        boolean isModified = false;
        boolean isEmailDuplicate = false;
        boolean isPhoneDuplicate = false;

        if (member.getName() != null && !Objects.equals(existingMember.getName(), member.getName())) {
            existingMember.setName(member.getName());
            isModified = true;
        }

        if (member.getEmail() != null && !Objects.equals(existingMember.getEmail(), member.getEmail())) {
            if (memberRepository.existsByEmail(member.getEmail())) {
                isEmailDuplicate = true;
            } else {
                existingMember.setEmail(member.getEmail());
                isModified = true;
            }
        }

        if (member.getPhone() != null && !Objects.equals(existingMember.getPhone(), member.getPhone())) {
            if (memberRepository.existsByPhone(member.getPhone())) {
                isPhoneDuplicate = true;
            } else {
                existingMember.setPhone(member.getPhone());
                isModified = true;
            }
        }

        if (member.getPassword() != null && !passwordEncoder.matches(member.getPassword(), existingMember.getPassword())) {
            existingMember.setPassword(passwordEncoder.encode(member.getPassword()));
            isModified = true;
        }

        if (isEmailDuplicate && isPhoneDuplicate) {
            throw new DataIntegrityViolationException("이미 사용 중인 이메일과 휴대폰번호입니다.");
        } else if (isEmailDuplicate) {
            throw new DataIntegrityViolationException("이미 사용 중인 이메일입니다.");
        } else if (isPhoneDuplicate) {
            throw new DataIntegrityViolationException("이미 사용 중인 휴대폰번호입니다.");
        }

        if (!isModified) {
            throw new DataIntegrityViolationException("변경된 회원 정보가 없습니다.");
        }

        return Optional.of(memberRepository.save(existingMember));
    }

    public void deleteById(Long memberId) {
        memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(memberId + "(으)로 등록된 회원이 없습니다."));
        memberRepository.deleteById(memberId);
    }

    public void logout(Long memberId) {
    }

}
