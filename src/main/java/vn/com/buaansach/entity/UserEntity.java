package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;
import vn.com.buaansach.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "bas_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private boolean activated = false;

    @Column(name = "disabled_by_admin", nullable = false)
    private boolean disabledByAdmin = false;

    @Size(min = 1, max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(min = 1, max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 255)
    @Column(unique = true)
    private String email;

    @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(min = 10, max = 12)
    @Column(unique = true)
    private String phone;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    private String activationKey;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @JsonIgnore
    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "bas_user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<AuthorityEntity> authorities = new HashSet<>();
}
