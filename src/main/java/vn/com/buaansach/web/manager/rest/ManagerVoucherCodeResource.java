package vn.com.buaansach.web.manager.rest;

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
import vn.com.buaansach.web.manager.service.ManagerVoucherCodeService;
import vn.com.buaansach.web.manager.service.dto.ManagerUpdateVoucherCodeDTO;

@Secured(AuthoritiesConstants.MANAGER)
@RestController
@RequestMapping("/api/v1/manager/voucher-code")
@RequiredArgsConstructor
public class ManagerVoucherCodeResource {
    private static final String ENTITY_NAME = "manager-voucher-code";
    private final Logger log = LoggerFactory.getLogger(ManagerVoucherCodeResource.class);
    private final ManagerVoucherCodeService managerVoucherCodeService;

    @PutMapping("/update-voucher-code")
    public ResponseEntity<Void> updateFirstRegVoucherCode(@RequestBody ManagerUpdateVoucherCodeDTO payload) {
        log.debug("REST request from user [{}] to update [{}]  : [{}] ", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        managerVoucherCodeService.updateFirstRegVoucherCode(payload);
        return ResponseEntity.ok().build();
    }
}
