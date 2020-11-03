package vn.com.buaansach.web.customer.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.customer.service.CustomerOrderService;

import java.util.UUID;

@Secured(AuthoritiesConstants.CUSTOMER)
@RestController
@RequestMapping("/api/v1/customer/order")
@RequiredArgsConstructor
public class CustomerOrderResource {
    private final String ENTITY_NAME = "customer-order";
    private final Logger log = LoggerFactory.getLogger(CustomerOrderResource.class);
    private final CustomerOrderService customerOrderService;

    @PutMapping("/update-user")
    public ResponseEntity<Void> updateOrderUser(@RequestBody String orderGuid) {
        log.debug("REST request from user [{}] to update phone [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        customerOrderService.updateOrderUser(UUID.fromString(orderGuid));
        return ResponseEntity.noContent().build();
    }
}
