package home.project.repository;
import home.project.domain.Member;
import home.project.domain.MemberDTOWithoutPw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long ID);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
