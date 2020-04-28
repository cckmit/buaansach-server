package vn.com.buaansach.web.customer.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.web.customer.service.CustomerOrderService;
import vn.com.buaansach.web.customer.service.dto.readwrite.CustomerOrderDTO;
import vn.com.buaansach.web.customer.service.dto.write.CustomerCancelOrderDTO;
import vn.com.buaansach.web.customer.service.dto.write.CustomerOrderUpdateDTO;

@RestController
@RequestMapping("/api/v1/customer/order")
public class CustomerOrderResource {
    private final String ENTITY_NAME = "customer-order";
    private final Logger log = LoggerFactory.getLogger(CustomerOrderResource.class);
    private final CustomerOrderService customerOrderService;

    public CustomerOrderResource(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @GetMapping("/get")
    public ResponseEntity<CustomerOrderDTO> getOrder(@RequestParam("orderGuid") String orderGuid,
                                                     @RequestParam("seatGuid") String seatGuid) {
        return ResponseEntity.ok(customerOrderService.getOrder(orderGuid, seatGuid));
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerOrderDTO> createOrder(@RequestBody String seatGuid) {
        return ResponseEntity.ok(customerOrderService.createOrder(seatGuid));
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerOrderDTO> updateOrder(@RequestBody CustomerOrderUpdateDTO payload) {
        return ResponseEntity.ok(customerOrderService.updateOrder(payload));
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestBody CustomerCancelOrderDTO payload) {
        customerOrderService.cancelOrder(payload);
        return ResponseEntity.ok().build();
    }


}
