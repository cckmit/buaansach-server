package vn.com.buaansach.web.customer.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.web.customer.service.CustomerSeatService;
import vn.com.buaansach.web.customer.service.dto.CustomerSeatDTO;

@RestController
@RequestMapping("/api/v1/customer/seat")
public class CustomerSeatResource {
    private final CustomerSeatService customerSeatService;

    public CustomerSeatResource(CustomerSeatService customerSeatService) {
        this.customerSeatService = customerSeatService;
    }

    @GetMapping("/get/{seatGuid}")
    public ResponseEntity<CustomerSeatDTO> getSeat(@PathVariable String seatGuid) {
        return ResponseEntity.ok(customerSeatService.getSeat(seatGuid));
    }

}
