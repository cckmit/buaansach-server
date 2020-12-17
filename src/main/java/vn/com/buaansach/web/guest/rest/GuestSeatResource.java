package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestSeatService;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestCheckOrderSeatDTO;

@RestController
@RequestMapping("/api/v1/guest/seat")
@RequiredArgsConstructor
public class GuestSeatResource {
    private final String ENTITY_NAME = "guest-seat";
    private final Logger log = LoggerFactory.getLogger(GuestSeatResource.class);
    private final GuestSeatService guestSeatService;

    @GetMapping("/get/{seatGuid}")
    public ResponseEntity<GuestSeatDTO> getSeat(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(guestSeatService.getSeat(seatGuid));
    }

    @PostMapping("/check-order-seat")
    public ResponseEntity<GuestCheckOrderSeatDTO> checkOrderSeat(@RequestBody GuestCheckOrderSeatDTO payload) {
        log.debug("REST request from user [{}] to check [{}] with list order guid: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(guestSeatService.checkOrderSeat(payload));
    }
}
