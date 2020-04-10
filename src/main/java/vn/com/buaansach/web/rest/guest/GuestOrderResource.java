package vn.com.buaansach.web.rest.guest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.service.dto.guest.GuestSeatDTO;
import vn.com.buaansach.service.guest.GuestOrderService;

@RestController
@RequestMapping("/api/v1/guest/order")
public class GuestOrderResource {
    private final GuestOrderService guestOrderService;

    public GuestOrderResource(GuestOrderService guestOrderService) {
        this.guestOrderService = guestOrderService;
    }

    @GetMapping("/seat/{seatGuid}")
    public ResponseEntity<GuestSeatDTO> getSeat(@PathVariable String seatGuid){
        return ResponseEntity.ok(guestOrderService.getSeat(seatGuid));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder() {
        return null;
    }

    @PostMapping("/cancel/{orderGuid}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderGuid) {
        return null;
    }
}
