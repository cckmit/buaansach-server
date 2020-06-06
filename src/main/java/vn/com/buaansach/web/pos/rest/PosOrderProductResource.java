package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosOrderProductService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductServeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductStatusChangeDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pos/order-product")
@RequiredArgsConstructor
public class PosOrderProductResource {
    private final String ENTITY_NAME = "pos-order-product";
    private final Logger log = LoggerFactory.getLogger(PosOrderProductResource.class);
    private final PosOrderProductService posOrderProductService;

    @GetMapping("/list-by-order/{orderGuid}")
    public ResponseEntity<List<PosOrderProductDTO>> getOrderProductByOrderGuid(@PathVariable String orderGuid) {
        log.debug("REST request from user [{}] to get [{}] by order : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        return ResponseEntity.ok(posOrderProductService.getOrderProductByOrderGuid(orderGuid));
    }

    @PutMapping("/serve")
    public ResponseEntity<Void> serveOrderProduct(@RequestBody PosOrderProductStatusChangeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to serve [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posOrderProductService.serveOrderProduct(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/serve-all")
    public ResponseEntity<Void> serveAllOrderProduct(@RequestBody PosOrderProductServeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to serve all [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posOrderProductService.serveAllOrderProduct(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrderProduct(@RequestBody PosOrderProductStatusChangeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to cancel [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posOrderProductService.cancelOrderProduct(payload, currentUser);
        return ResponseEntity.ok().build();
    }
}
