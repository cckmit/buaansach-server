package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestStoreService;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCallWaiterDTO;

@RestController
@RequestMapping("/api/v1/guest/store")
@RequiredArgsConstructor
public class GuestStoreResource {
    private final String ENTITY_NAME = "guest-store";
    private final Logger log = LoggerFactory.getLogger(GuestStoreResource.class);
    private final GuestStoreService guestStoreService;

    @GetMapping("/get-by-seat/{seatGuid}")
    public ResponseEntity<GuestStoreDTO> getStoreBySeat(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to get [{}] by seat : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(guestStoreService.getStoreBySeat(seatGuid));
    }

    @PostMapping("/call-waiter")
    public ResponseEntity<Void> callWaiter(@RequestBody GuestCallWaiterDTO payload){
        log.debug("REST request from user [{}] to call waiter of [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        guestStoreService.callWaiter(payload);
        return ResponseEntity.noContent().build();
    }
}
