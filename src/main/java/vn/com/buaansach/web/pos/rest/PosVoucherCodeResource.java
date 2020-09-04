package vn.com.buaansach.web.pos.rest;

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
import vn.com.buaansach.web.pos.service.PosVoucherCodeService;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherApplySuccessDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderVoucherCodeDTO;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/voucher-code")
@RequiredArgsConstructor
public class PosVoucherCodeResource {
    private final String ENTITY_NAME = "pos-voucher-code";
    private final Logger log = LoggerFactory.getLogger(PosVoucherCodeResource.class);
    private final PosVoucherCodeService posVoucherCodeService;

    @PutMapping("/apply-voucher")
    public ResponseEntity<PosVoucherApplySuccessDTO> applyVoucherCode(@Valid @RequestBody PosOrderVoucherCodeDTO payload) {
        log.debug("REST request from user [{}] to apply [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(posVoucherCodeService.applyOrderVoucherCode(payload));
    }

    @PutMapping("/cancel-voucher")
    public ResponseEntity<Void> cancelVoucher(@Valid @RequestBody PosOrderVoucherCodeDTO payload) {
        log.debug("REST request from user [{}] to cancel [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        posVoucherCodeService.cancelVoucherCode(payload);
        return ResponseEntity.ok().build();
    }
}
