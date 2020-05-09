package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosOrderService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;
import vn.com.buaansach.web.pos.service.dto.write.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/pos/order")
public class PosOrderResource {
    private final String ENTITY_NAME = "pos-order";
    private final Logger log = LoggerFactory.getLogger(PosOrderResource.class);
    private final PosOrderService posOrderService;

    public PosOrderResource(PosOrderService posOrderService) {
        this.posOrderService = posOrderService;
    }

    @PostMapping("/create")
    public ResponseEntity<PosOrderDTO> createOrder(@Valid @RequestBody PosOrderCreateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to create {} : {}", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posOrderService.createOrder(payload, currentUser));
    }

    @PutMapping("/update")
    public ResponseEntity<PosOrderDTO> updateOrder(@Valid @RequestBody PosOrderUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to update {} : {}", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posOrderService.updateOrder(payload, currentUser));
    }

//    @GetMapping("/get/{orderGuid}")
//    public ResponseEntity<PosOrderDTO> getOrder(@PathVariable String orderGuid) {
//        log.debug("REST request from user [{}] to get {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
//        return ResponseEntity.ok(posOrderService.getOrder(orderGuid));
//    }

    @GetMapping("/get-seat-order/{seatGuid}")
    public ResponseEntity<PosOrderDTO> getSeatCurrentOrder(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to get {} by seat : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(posOrderService.getSeatCurrentOrder(seatGuid));
    }

    @PutMapping("/change-seat")
    public ResponseEntity<Void> changeSeat(@Valid @RequestBody PosOrderSeatChangeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to change seat for {} : {}", currentUser, ENTITY_NAME, payload);
        posOrderService.changeSeat(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/receive")
    public ResponseEntity<Void> receiveOrder(@RequestBody String orderGuid) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to receive {} : {}", currentUser, ENTITY_NAME, orderGuid);
        posOrderService.receiveOrder(orderGuid, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/purchase")
    public ResponseEntity<Void> purchaseOrder(@Valid @RequestBody PosOrderPurchaseDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to purchase {} : {}", currentUser, ENTITY_NAME, payload);
        posOrderService.purchaseOrder(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestBody PosOrderCancelDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to cancel {} : {}", currentUser, ENTITY_NAME, payload);
        posOrderService.cancelOrder(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-customer-phone")
    public ResponseEntity<Void> changeCustomerPhone(@RequestBody PosOrderCustomerPhoneChangeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to change customer phone for {} : {}", currentUser, ENTITY_NAME, payload);
        posOrderService.changeCustomerPhone(payload, currentUser);
        return ResponseEntity.ok().build();
    }
}
