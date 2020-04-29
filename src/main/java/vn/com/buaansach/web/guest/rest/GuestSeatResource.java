package vn.com.buaansach.web.guest.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.web.guest.service.GuestSeatService;
import vn.com.buaansach.web.guest.service.dto.GuestSeatDTO;

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
        return ResponseEntity.ok(guestSeatService.getSeat(seatGuid));
    }

    @PutMapping("/change-seat")
    public ResponseEntity<Void> changeSeat() {
        return null;
    }

}
