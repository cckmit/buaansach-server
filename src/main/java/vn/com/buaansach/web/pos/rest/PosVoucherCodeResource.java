package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosVoucherService;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;

@RestController
@RequestMapping("/api/v1/pos/voucher-code")
public class PosVoucherCodeResource {
    private final String ENTITY_NAME = "pos-voucher-code";
    private final Logger log = LoggerFactory.getLogger(PosStoreResource.class);
    private final PosVoucherService posVoucherService;

    public PosVoucherCodeResource(PosVoucherService posVoucherService) {
        this.posVoucherService = posVoucherService;
    }

    @GetMapping("/get")
    public ResponseEntity<PosVoucherCodeDTO> getVoucherCodeInfo(@RequestParam String storeGuid,
                                                                @RequestParam String voucherCode) {
        log.debug("REST request from user {} to get {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, voucherCode);
        return ResponseEntity.ok(posVoucherService.getVoucherCodeInfo(storeGuid, voucherCode));
    }
}
