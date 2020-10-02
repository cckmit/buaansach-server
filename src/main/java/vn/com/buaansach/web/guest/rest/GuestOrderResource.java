package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestOrderService;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCreateOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestOrderUpdateDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/guest/order")
@RequiredArgsConstructor
public class GuestOrderResource {
    private final String ENTITY_NAME = "guest-order";
    private final Logger log = LoggerFactory.getLogger(GuestOrderResource.class);
    private final GuestOrderService guestOrderService;

    @GetMapping("/get/{orderGuid}")
    public ResponseEntity<GuestOrderDTO> getOrder(@PathVariable String orderGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        return ResponseEntity.ok(guestOrderService.getOrder(orderGuid));
    }

    @PostMapping("/create")
    public ResponseEntity<GuestOrderDTO> createOrder(@Valid @RequestBody GuestCreateOrderDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to create [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(guestOrderService.createOrder(payload, currentUser));
    }

    @PutMapping("/update")
    public ResponseEntity<GuestOrderDTO> updateOrder(@Valid @RequestBody GuestOrderUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to update [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(guestOrderService.updateOrder(payload, currentUser));
    }

}
