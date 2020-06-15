package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.rest.GuestStoreResource;
import vn.com.buaansach.web.pos.service.PosVoucherService;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherDTO;

@Secured(AuthoritiesConstants.USER)
@RestController
@RequestMapping("/api/v1/pos/voucher")
@RequiredArgsConstructor
public class PosVoucherResource {
    private final String ENTITY_NAME = "pos-voucher";
    private final Logger log = LoggerFactory.getLogger(GuestStoreResource.class);
    private final PosVoucherService posVoucherService;

    @GetMapping("/get-first-register-voucher")
    public ResponseEntity<PosVoucherDTO> getFirstRegisterVoucher() {
        log.debug("REST request from user [{}] to get fist register [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(posVoucherService.getFirstRegisterVoucher());
    }
}
