package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosStoreUserService;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pos/store-user")
@RequiredArgsConstructor
public class PosStoreUserResource {
    private final String ENTITY_NAME = "pos-store-user";
    private final Logger log = LoggerFactory.getLogger(PosStoreUserResource.class);
    private final PosStoreUserService posStoreUserService;

    /**
     * Dùng cho trang thống kê
     */
    @GetMapping("/get/{storeGuid}")
    public ResponseEntity<List<PosStoreUserDTO>> getListStoreUser(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posStoreUserService.getListStoreUser(storeGuid));
    }

    /**
     * Dùng cho trang thống kê, dựa theo role của user đối với cửa hàng để cho phép xem báo cáo các nhân viên khác
     */
    @GetMapping("/get-current-store-user-role/{storeGuid}")
    public ResponseEntity<String> getCurrentStoreUserRole(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to get current [{}] role : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posStoreUserService.getCurrentStoreUserRole(storeGuid));
    }
}
