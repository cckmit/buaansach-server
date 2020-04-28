package vn.com.buaansach.web.customer.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.web.customer.service.CustomerSeatService;
import vn.com.buaansach.web.customer.service.dto.CustomerSeatDTO;

@RestController
@RequestMapping("/api/v1/customer/seat")
public class CustomerSeatResource {
    private final String ENTITY_NAME = "customer-seat";
    private final Logger log = LoggerFactory.getLogger(CustomerSeatResource.class);
    private final CustomerSeatService customerSeatService;

    public CustomerSeatResource(CustomerSeatService customerSeatService) {
        this.customerSeatService = customerSeatService;
    }

    @GetMapping("/get/{seatGuid}")
    public ResponseEntity<CustomerSeatDTO> getSeat(@PathVariable String seatGuid) {
        return ResponseEntity.ok(customerSeatService.getSeat(seatGuid));
    }

    @PutMapping("/change-seat")
    public ResponseEntity<Void> changeSeat() {
        return null;
    }

}
