package vn.com.buaansach.web.customer.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.web.customer.service.dto.CustomerSeatDTO;
import vn.com.buaansach.web.customer.service.CustomerSeatService;

@RestController
@RequestMapping("/api/v1/guest/seat")
public class CustomerSeatResource {
    private final CustomerSeatService customerSeatService;

    public CustomerSeatResource(CustomerSeatService customerSeatService) {
        this.customerSeatService = customerSeatService;
    }

    @GetMapping("/get/{seatGuid}")
    public ResponseEntity<CustomerSeatDTO> getSeat(@PathVariable String seatGuid){
        return ResponseEntity.ok(customerSeatService.getSeat(seatGuid));
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
