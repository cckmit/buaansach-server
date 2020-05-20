package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminVoucherInventoryService;
import vn.com.buaansach.web.admin.service.dto.read.AdminVoucherInventoryStatusDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminVoucherInventoryGenerateDTO;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/voucher-inventory")
@RequiredArgsConstructor
public class AdminVoucherInventoryResource {
    private final String ENTITY_NAME = "admin-voucher-inventory";
    private final Logger log = LoggerFactory.getLogger(AdminUserResource.class);
    private final AdminVoucherInventoryService adminVoucherInventoryService;

    @PostMapping("/generate")
    public ResponseEntity<Void> generateVoucherInventory(@RequestBody AdminVoucherInventoryGenerateDTO payload) {
        log.debug("REST request from user [{}] to generate [{}]: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminVoucherInventoryService.generateVoucherInventory(payload);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<AdminVoucherInventoryStatusDTO> getVoucherInventoryStatus() {
        log.debug("REST request from user [{}] to get status of [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminVoucherInventoryService.countRemainVoucherCode());
    }

}
