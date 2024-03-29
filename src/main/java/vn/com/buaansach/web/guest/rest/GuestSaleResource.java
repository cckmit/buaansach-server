package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestSaleService;

@RestController
@RequestMapping("/api/v1/guest/sale")
@RequiredArgsConstructor
public class GuestSaleResource {
    private final String ENTITY_NAME = "guest-sale";
    private final Logger log = LoggerFactory.getLogger(GuestSaleResource.class);
    private final GuestSaleService getSaleService;

    @GetMapping("/get/{saleGuid}")
    public ResponseEntity<SaleEntity> getSale(@PathVariable String saleGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, saleGuid);
        return ResponseEntity.ok(getSaleService.getSale(saleGuid));
    }

    @GetMapping("/get-with-store")
    public ResponseEntity<SaleEntity> getSaleWithStore(@RequestParam("saleGuid") String saleGuid, @RequestParam("storeGuid") String storeGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}] with store : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, saleGuid, storeGuid);
        return ResponseEntity.ok(getSaleService.getSaleWithStore(saleGuid, storeGuid));
    }
}
