package vn.com.buaansach.web.guest.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestSeatService;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;

@RestController
@RequestMapping("/api/v1/guest/seat")
public class GuestSeatResource {
    private final String ENTITY_NAME = "guest-seat";
    private final Logger log = LoggerFactory.getLogger(GuestSeatResource.class);
    private final GuestSeatService guestSeatService;

    public GuestSeatResource(GuestSeatService guestSeatService) {
        this.guestSeatService = guestSeatService;
    }

    @GetMapping("/get/{seatGuid}")
    public ResponseEntity<GuestSeatDTO> getSeat(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(guestSeatService.getSeat(seatGuid));
    }

    @GetMapping("/is-order-matches-seat")
    public ResponseEntity<Boolean> isOrderMatchesSeat(@RequestParam("orderGuid") String orderGuid,
                                                    @RequestParam("seatGuid") String seatGuid) {
        log.debug("REST request from user [{}] to check [{}] : [{}] matches order : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid, orderGuid);
        return ResponseEntity.ok(guestSeatService.isOrderMatchesSeat(orderGuid, seatGuid));
    }
//
//    @PutMapping("/change-seat")
//    public ResponseEntity<Void> changeSeat() {
//        return null;
//    }

}
