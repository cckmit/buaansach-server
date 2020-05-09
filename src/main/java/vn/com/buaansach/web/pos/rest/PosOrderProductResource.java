package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosOrderProductService;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductStatusChangeDTO;

@RestController
@RequestMapping("/api/v1/pos/order-product")
public class PosOrderProductResource {
    private final String ENTITY_NAME = "pos-order-product";
    private final Logger log = LoggerFactory.getLogger(PosOrderProductResource.class);
    private final PosOrderProductService posOrderProductService;

    public PosOrderProductResource(PosOrderProductService posOrderProductService) {
        this.posOrderProductService = posOrderProductService;
    }

    @PutMapping("/serve")
    public ResponseEntity<Void> serveOrderProduct(@RequestBody PosOrderProductStatusChangeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to serve {} : {}", currentUser, ENTITY_NAME, payload);
        posOrderProductService.serveOrderProduct(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrderProduct(@RequestBody PosOrderProductStatusChangeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to cancel {} : {}", currentUser, ENTITY_NAME, payload);
        posOrderProductService.cancelOrderProduct(payload, currentUser);
        return ResponseEntity.ok().build();
    }
}
