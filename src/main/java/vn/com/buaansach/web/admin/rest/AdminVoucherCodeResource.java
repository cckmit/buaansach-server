package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminVoucherCodeService;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateVoucherCodeDTO;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/voucher-code")
@RequiredArgsConstructor
public class AdminVoucherCodeResource {
    private final String ENTITY_NAME = "admin-voucher-code";
    private final Logger log = LoggerFactory.getLogger(AdminVoucherCodeResource.class);
    private final AdminVoucherCodeService adminVoucherCodeService;

    @PutMapping("/toggle-voucher-code")
    public ResponseEntity<Void> toggleVoucherCode(@RequestBody String voucherCode) {
        log.debug("REST request from user [{}] to toggle [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, voucherCode);
        adminVoucherCodeService.toggleVoucherCode(voucherCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateVoucherCode(@RequestBody AdminUpdateVoucherCodeDTO payload) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminVoucherCodeService.updateVoucherCode(payload);
        return ResponseEntity.ok().build();
    }

}
