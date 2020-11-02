package vn.com.buaansach.web.admin.service.dto.write;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class AdminUpdateStoreWorkShiftUserDTO {
    private UUID storeWorkShiftGuid;
    private List<String> listUser;
    private List<String> listWorkDay;
}
