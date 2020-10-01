package vn.com.buaansach.web.customer.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.customer.service.write.CustomerUsePointDTO;
import vn.com.buaansach.web.shared.service.CustomerService;

@Secured(AuthoritiesConstants.CUSTOMER)
@RestController
@RequestMapping("/api/v1/customer/info")
@RequiredArgsConstructor
public class CustomerInfoResource {
    private final String ENTITY_NAME = "customer-info";
    private final Logger log = LoggerFactory.getLogger(CustomerInfoResource.class);
    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<CustomerEntity> getCustomerInfo() {
        log.debug("REST request from user [{}] to get [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(customerService.getCustomerInfo());
    }

    @PostMapping("/use-point")
    public ResponseEntity<Void> usePoint(@RequestBody CustomerUsePointDTO payload){
        log.debug("REST request from user [{}] to use [{}] point", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        customerService.assignPoint(payload);
        return ResponseEntity.ok().build();
    }
}
