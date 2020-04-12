package vn.com.buaansach.web.customer.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.web.customer.service.dto.GuestSeatDTO;
import vn.com.buaansach.web.customer.service.CustomerOrderService;

@RestController
@RequestMapping("/api/v1/guest/order")
public class CustomerOrderResource {
    private final CustomerOrderService customerOrderService;

    public CustomerOrderResource(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @GetMapping("/seat/{seatGuid}")
    public ResponseEntity<GuestSeatDTO> getSeat(@PathVariable String seatGuid){
        return ResponseEntity.ok(customerOrderService.getSeat(seatGuid));
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
