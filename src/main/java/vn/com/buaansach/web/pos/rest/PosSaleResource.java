package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosSaleService;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosApplySaleDTO;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/pos/sale")
@RequiredArgsConstructor
public class PosSaleResource {
    private final String ENTITY_NAME = "pos-sale";
    private final Logger log = LoggerFactory.getLogger(PosSaleResource.class);
    private final PosSaleService posSaleService;

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<PosSaleDTO>> getListStoreSale(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list [{}]: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posSaleService.getListStoreSaleByStore(storeGuid));
    }

    @PutMapping("/apply")
    public ResponseEntity<Void> applySale(@RequestBody PosApplySaleDTO payload) {
        log.debug("REST request from user [{}] to apply [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        posSaleService.applySale(payload);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelSale(@RequestBody String orderGuid) {
        log.debug("REST request from user [{}] to apply [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        posSaleService.cancelSale(UUID.fromString(orderGuid));
        return ResponseEntity.noContent().build();
    }
}
