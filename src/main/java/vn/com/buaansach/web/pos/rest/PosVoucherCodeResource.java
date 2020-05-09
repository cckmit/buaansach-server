package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosVoucherCodeService;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherApplySuccessDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderVoucherCodeDTO;

@RestController
@RequestMapping("/api/v1/pos/voucher-code")
public class PosVoucherCodeResource {
    private final String ENTITY_NAME = "pos-voucher-code";
    private final Logger log = LoggerFactory.getLogger(PosVoucherCodeResource.class);
    private final PosVoucherCodeService posVoucherCodeService;

    public PosVoucherCodeResource(PosVoucherCodeService posVoucherCodeService) {
        this.posVoucherCodeService = posVoucherCodeService;
    }
//
//    @GetMapping("/get/{voucherCode}")
//    public ResponseEntity<PosVoucherCodeDTO> getVoucherCodeInfo(@PathVariable String voucherCode) {
//        log.debug("REST request from user [{}] to get {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, voucherCode);
//        return ResponseEntity.ok(posVoucherCodeService.getVoucherCodeInfo(voucherCode));
//    }

    @PutMapping("/apply-voucher")
    public ResponseEntity<PosVoucherApplySuccessDTO> applyVoucherCode(@RequestBody PosOrderVoucherCodeDTO payload) {
        log.debug("REST request from user [{}] to apply {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(posVoucherCodeService.applyOrderVoucherCode(payload));
    }

    @PutMapping("/cancel-voucher")
    public ResponseEntity<Void> cancelVoucher(@RequestBody PosOrderVoucherCodeDTO payload) {
        log.debug("REST request from user [{}] to cancel {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        posVoucherCodeService.cancelVoucherCode(payload);
        return ResponseEntity.ok().build();
    }
}
