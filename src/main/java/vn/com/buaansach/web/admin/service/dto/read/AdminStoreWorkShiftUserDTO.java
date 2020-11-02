package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.store.StoreWorkShiftUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;

import java.util.UUID;

@Data
public class AdminStoreWorkShiftUserDTO {
    private String workDay;
    private UUID storeGuid;
    private UUID storeWorkShiftGuid;
    private String userLogin;

    /* User account */
    private String userEmail;
    private String userPhone;

    /* User Profile */
    private String userCode;
    private String fullName;
    private String avatarUrl;

    public AdminStoreWorkShiftUserDTO() {
    }

    public AdminStoreWorkShiftUserDTO(StoreWorkShiftUserEntity entity, UserEntity user, UserProfileEntity profile) {
        this.workDay = entity.getWorkDay();
        this.storeGuid = entity.getStoreGuid();
        this.userLogin = entity.getUserLogin();
        this.storeWorkShiftGuid = entity.getStoreWorkShiftGuid();

        this.userEmail = user.getUserEmail();
        this.userPhone = user.getUserPhone();

        this.userCode = profile.getUserCode();
        this.fullName = profile.getFullName();
        this.avatarUrl = profile.getAvatarUrl();
    }
}
