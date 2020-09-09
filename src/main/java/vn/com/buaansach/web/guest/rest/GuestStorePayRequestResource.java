package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestStorePayRequestNotificationService;
import vn.com.buaansach.web.guest.service.dto.write.GuestStorePayRequestDTO;

@RestController
@RequestMapping("/api/v1/guest/store-pay-request")
@RequiredArgsConstructor
public class GuestStorePayRequestResource {
    private final String ENTITY_NAME = "store-pay-request";
    private final Logger log = LoggerFactory.getLogger(GuestStorePayRequestResource.class);
    private final GuestStorePayRequestNotificationService guestStorePayRequestNotificationService;

    @PostMapping("/send-request")
    public ResponseEntity<GuestStorePayRequestDTO> sendRequest(@RequestBody GuestStorePayRequestDTO payload){
        log.debug("REST request from user [{}] to send [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(guestStorePayRequestNotificationService.sendRequest(payload));
    }

    @GetMapping("/get-by-order/{orderGuid}")
    public ResponseEntity<GuestStorePayRequestDTO> getByOrderGuid(@PathVariable String orderGuid){
        log.debug("REST request from user [{}] to get [{}] by order : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        return ResponseEntity.ok(guestStorePayRequestNotificationService.getByOrderGuid(orderGuid));
    }
}
