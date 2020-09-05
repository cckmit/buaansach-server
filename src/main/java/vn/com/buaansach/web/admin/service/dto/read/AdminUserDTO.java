package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.enumeration.UserType;
import vn.com.buaansach.entity.user.AuthorityEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AdminUserDTO extends AuditDTO {
    private UUID guid;
    private String userLogin;
    private String userEmail;
    private String userPhone;
    private boolean userActivated;
    private UserType userType;
    private Set<String> authorities;

    private String userCode;
    private String fullName;
    private String avatarUrl;
    private Gender userGender;
    private Instant userBirthday;
    private String userAddress;
    private String langKey;

    public AdminUserDTO(UserEntity userEntity, UserProfileEntity profile) {
        this.guid = userEntity.getGuid();
        this.userLogin = userEntity.getUserLogin();
        this.userEmail = userEntity.getUserEmail();
        this.userPhone = userEntity.getUserPhone();
        this.userActivated = userEntity.isUserActivated();
        this.userType = userEntity.getUserType();
        this.authorities = userEntity.getAuthorities().stream()
                .map(AuthorityEntity::getName)
                .collect(Collectors.toSet());

        this.userCode = profile.getUserCode();
        this.fullName = profile.getFullName();
        this.avatarUrl = profile.getFullName();
        this.userGender = profile.getUserGender();
        this.userBirthday = profile.getUserBirthday();
        this.userAddress = profile.getUserAddress();
        this.langKey = profile.getLangKey();

        this.createdBy = userEntity.getCreatedBy();
        this.createdDate = userEntity.getCreatedDate();
        this.lastModifiedBy = userEntity.getLastModifiedBy();
        this.lastModifiedDate = userEntity.getLastModifiedDate();
    }
}
