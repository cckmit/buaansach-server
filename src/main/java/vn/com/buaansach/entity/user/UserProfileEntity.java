package vn.com.buaansach.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.Gender;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bas_user_profile")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserProfileEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Size(min = 1, max = 20)
    @Column(unique = true)
    private String userCode;

    @Size(min = 1, max = 100)
    @Column(name = "full_name", length = 100)
    private String fullName;

    @Size(max = 255)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    private Gender userGender = Gender.UNDEFINED;

    @Column(name = "user_birthday")
    private Instant userBirthday;

    @Size(max = 255)
    @Column(name = "user_address")
    private String userAddress;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    /**
     * FK
     */

    @Column(name = "user_guid")
    private UUID userGuid;

}
