package vn.com.buaansach.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.model.dto.core.AuditDTO;
import vn.com.buaansach.model.entity.AuthorityEntity;
import vn.com.buaansach.model.entity.UserEntity;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AuditDTO {

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

    @Size(min = 2, max = 10)
    private String langKey;

    @Size(max = 255)
    private String imageUrl;

    private Set<String> authorities;

    public UserDTO(UserEntity userEntity) {
        this.login = userEntity.getLogin();
        this.activated = userEntity.isActivated();
        this.disabledByAdmin = userEntity.isDisabledByAdmin();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
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
