package vn.com.buaansach.web.shared.service.dto.read;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.user.AuthorityEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;

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
    private String userLogin;
    private String userEmail;
    private String userPhone;
    private Set<String> authorities;

    private String fullName;
    private String avatarUrl;
    private Gender userGender;
    private Instant userBirthday;
    private String userAddress;
    private String langKey;

    public UserDTO(UserEntity userEntity, UserProfileEntity profileEntity) {
        this.guid = userEntity.getGuid();
        this.userLogin = userEntity.getUserLogin();
        this.userEmail = userEntity.getUserEmail();
        this.userPhone = userEntity.getUserPhone();
        this.authorities = userEntity.getAuthorities().stream()
                .map(AuthorityEntity::getName)
                .collect(Collectors.toSet());

        this.fullName = profileEntity.getFullName();
        this.avatarUrl = profileEntity.getAvatarUrl();
        this.userGender = profileEntity.getUserGender();
        this.userBirthday = profileEntity.getUserBirthday();
        this.userAddress = profileEntity.getUserAddress();
        this.langKey = profileEntity.getLangKey();

        this.createdBy = userEntity.getCreatedBy();
        this.createdDate = userEntity.getCreatedDate();
        this.lastModifiedBy = userEntity.getLastModifiedBy();
        this.lastModifiedDate = userEntity.getLastModifiedDate();
    }
}
