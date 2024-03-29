package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosOrderService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosListOrderDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;
import vn.com.buaansach.web.pos.service.dto.write.*;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/order")
@RequiredArgsConstructor
public class PosOrderResource {
    private final String ENTITY_NAME = "pos-order";
    private final Logger log = LoggerFactory.getLogger(PosOrderResource.class);
    private final PosOrderService posOrderService;

    @PostMapping("/create")
    public ResponseEntity<PosOrderDTO> createOrder(@Valid @RequestBody PosOrderCreateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to create [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posOrderService.createOrder(payload, currentUser));
    }

    @PutMapping("/update")
    public ResponseEntity<PosOrderDTO> updateOrder(@Valid @RequestBody PosOrderUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to update [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posOrderService.updateOrder(payload, currentUser));
    }

    @GetMapping("/get-seat-order/{seatGuid}")
    public ResponseEntity<PosOrderDTO> getSeatOrder(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to get [{}] by seat : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(posOrderService.getSeatOrder(seatGuid));
    }

    @PutMapping("/change-seat")
    public ResponseEntity<Void> changeSeat(@Valid @RequestBody PosOrderSeatChangeDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to change seat for [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posOrderService.changeSeat(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/receive")
    public ResponseEntity<Void> receiveOrder(@RequestBody String orderGuid) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to receive [{}] : [{}]", currentUser, ENTITY_NAME, orderGuid);
        posOrderService.receiveOrder(orderGuid, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/purchase")
    public ResponseEntity<Void> purchaseOrder(@Valid @RequestBody PosOrderPurchaseDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to purchase [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posOrderService.purchaseOrder(payload, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@Valid @RequestBody PosOrderCancelDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to cancel [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posOrderService.cancelOrder(payload, currentUser);
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/change-customer-phone")
//    public ResponseEntity<Void> changeCustomerPhone(@Valid @RequestBody PosOrderCustomerPhoneChangeDTO payload) {
//        String currentUser = SecurityUtils.getCurrentUserLogin();
//        log.debug("REST request from user [{}] to change customer phone for [{}] : [{}]", currentUser, ENTITY_NAME, payload);
//        posOrderService.changeCustomerPhone(payload, currentUser);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/get-list-order-by-list-seat")
    public ResponseEntity<List<PosOrderDTO>> getListOrderByListSeat(@Valid @RequestBody PosListOrderDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to get list [{}] by list seat : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posOrderService.getListOrderByListSeat(payload));
    }

    @PutMapping("/purchase-group-order")
    public ResponseEntity<Void> purchaseListOrder(@Valid @RequestBody PosPurchaseGroupDTO payload){
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to purchase list [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posOrderService.purchaseGroupOrder(payload);
        return ResponseEntity.noContent().build();
    }
}
