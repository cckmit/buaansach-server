package vn.com.buaansach.web.admin.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminVoucherService;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminVoucherDTO;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/voucher")
public class AdminVoucherResource {
    private final String ENTITY_NAME = "admin-voucher";
    private final Logger log = LoggerFactory.getLogger(AdminUserResource.class);
    private final AdminVoucherService adminVoucherService;

    public AdminVoucherResource(AdminVoucherService adminVoucherService) {
        this.adminVoucherService = adminVoucherService;
    }

    @PostMapping("/create")
    public ResponseEntity<AdminVoucherDTO> createVoucher(@Valid @RequestBody AdminVoucherDTO payload) {
        log.debug("REST request from user [{}] to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminVoucherService.createVoucher(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<AdminVoucherDTO> updateVoucher(@Valid @RequestBody AdminVoucherDTO payload) {
        log.debug("REST request from user [{}] to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminVoucherService.updateVoucher(payload));
    }

    @PutMapping("/toggle-voucher")
    public ResponseEntity<Void> toggleVoucher(@RequestBody String voucherGuid) {
        log.debug("REST request from user [{}] to toggle {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, voucherGuid);
        adminVoucherService.toggleVoucher(voucherGuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminVoucherDTO>> getListVoucher() {
        log.debug("REST request from user [{}] to list {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminVoucherService.getListVoucher());
    }

    @GetMapping("/list-voucher-code/{voucherGuid}")
    public ResponseEntity<List<VoucherCodeEntity>> getListVoucherCodeByVoucherGuid(@PathVariable String voucherGuid){
        log.debug("REST request from user [{}] to list voucher code by {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, voucherGuid);
        return ResponseEntity.ok(adminVoucherService.getListVoucherCodeByVoucherGuid(voucherGuid));
    }
}
