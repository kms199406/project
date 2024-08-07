package home.project.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = {"phone", "email"})})

@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Column(name = "password")//, columnDefinition = ("VARCHAR(255)"))
    private String password;

    @NotEmpty(message = "이름을 입력해주세요.")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "전화번호를 입력해주세요.")
    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;
}
