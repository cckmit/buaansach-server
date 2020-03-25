package vn.com.buaansach.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AuthorityEntity;
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.service.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotBlank
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    @NotBlank
    private String firstName;

    @Size(max = 50)
    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    @Size(min = 5, max = 255)
    private String email;

    @Size(min = 10, max = 12)
    private String phone;

    private boolean activated;

    @Size(min = 2, max = 10)
    private String langKey;

    @Size(max = 255)
    private String imageUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant lastModifiedDate;

    private Set<String> authorities;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.login = userEntity.getLogin();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
        this.activated = userEntity.isActivated();
        this.langKey = userEntity.getLangKey();
        this.imageUrl = userEntity.getImageUrl();
        this.createdBy = userEntity.getCreatedBy();
        this.createdDate = userEntity.getCreatedDate();
        this.lastModifiedBy = userEntity.getLastModifiedBy();
        this.lastModifiedDate = userEntity.getLastModifiedDate();
        this.authorities = userEntity.getAuthorities().stream()
                .map(AuthorityEntity::getName)
                .collect(Collectors.toSet());
    }
}
