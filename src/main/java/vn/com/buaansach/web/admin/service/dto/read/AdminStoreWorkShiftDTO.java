package vn.com.buaansach.web.admin.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.store.StoreWorkShiftEntity;
import vn.com.buaansach.web.shared.service.dto.AuditDTO;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminStoreWorkShiftDTO extends AuditDTO {
    private UUID guid;
    private String storeWorkShiftName;
    private String storeWorkShiftDescription;
    private String storeWorkShiftStart;
    private String storeWorkShiftEnd;
    private UUID storeGuid;

    private List<AdminStoreWorkShiftUserDTO> listUser;

    public AdminStoreWorkShiftDTO() {
    }

    public AdminStoreWorkShiftDTO(StoreWorkShiftEntity entity) {
        this.guid = entity.getGuid();
        this.storeWorkShiftName = entity.getStoreWorkShiftName();
        this.storeWorkShiftDescription = entity.getStoreWorkShiftDescription();
        this.storeWorkShiftStart = entity.getStoreWorkShiftStart();
        this.storeWorkShiftEnd = entity.getStoreWorkShiftEnd();
        this.storeGuid = entity.getStoreGuid();
    }
}
