package vn.com.buaansach.web.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.user.AuthorityEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AuditDTO {
    private UUID guid;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 255)
    private String email;

    @Size(min = 10, max = 12)
    private String phone;

    private boolean activated;

    private boolean disabledByAdmin;

    private Gender gender;

    private Instant birthday;

    private String address;

    @Size(min = 2, max = 10)
    private String langKey;

    @Size(max = 255)
    private String imageUrl;

    private Set<String> authorities;

    public UserDTO(UserEntity userEntity) {
        this.guid = userEntity.getGuid();
        this.login = userEntity.getLogin();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
        this.activated = userEntity.isActivated();
        this.disabledByAdmin = userEntity.isDisabledByAdmin();
        this.gender = userEntity.getGender();
        this.birthday = userEntity.getBirthday();
        this.address = userEntity.getAddress();
        this.langKey = userEntity.getLangKey();
        this.imageUrl = userEntity.getImageUrl();
        this.authorities = userEntity.getAuthorities().stream()
                .map(AuthorityEntity::getName)
                .collect(Collectors.toSet());

        this.createdBy = userEntity.getCreatedBy();
        this.createdDate = userEntity.getCreatedDate();
        this.lastModifiedBy = userEntity.getLastModifiedBy();
        this.lastModifiedDate = userEntity.getLastModifiedDate();
    }
}
