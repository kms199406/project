package home.project.repository;
import home.project.domain.Member;
import home.project.domain.MemberDTOWithoutPw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long ID);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("SELECT m FROM Member m WHERE " +
            "(:name IS NULL OR :name = '' OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR :email = '' OR LOWER(m.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phone IS NULL OR :phone = '' OR LOWER(m.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) AND " +
            "(:query IS NULL OR :query = '' OR " +
            "(LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.phone) LIKE LOWER(CONCAT('%', :query, '%'))))")
    Page<Member> findMembers(@Param("name") String name,
                             @Param("email") String email,
                             @Param("phone") String phone,
                             @Param("query") String query,
                             Pageable pageable);
}
