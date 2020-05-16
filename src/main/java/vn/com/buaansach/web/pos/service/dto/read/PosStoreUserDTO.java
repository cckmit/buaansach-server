package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.store.StoreUserEntity;

import java.util.UUID;

@Data
public class PosStoreUserDTO {
    private UUID guid;
    private StoreUserRole storeUserRole;
    private StoreUserStatus storeUserStatus;
    private UUID storeGuid;
    private String userLogin;

    public PosStoreUserDTO() {

    }

    public PosStoreUserDTO(StoreUserEntity storeUserEntity) {
        this.guid = storeUserEntity.getGuid();
        this.storeUserRole = storeUserEntity.getStoreUserRole();
        this.storeUserStatus = storeUserEntity.getStoreUserStatus();
        this.storeGuid = storeUserEntity.getStoreGuid();
        this.userLogin = storeUserEntity.getUserLogin();
    }
}
