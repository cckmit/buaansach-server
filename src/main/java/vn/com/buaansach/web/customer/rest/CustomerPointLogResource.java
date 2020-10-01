package vn.com.buaansach.web.customer.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.entity.customer.CustomerPointLogEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.shared.service.CustomerService;

import java.util.List;

@Secured(AuthoritiesConstants.CUSTOMER)
@RestController
@RequestMapping("/api/v1/customer/point-log")
@RequiredArgsConstructor
public class CustomerPointLogResource {
    private final String ENTITY_NAME = "customer-point-log";
    private final Logger log = LoggerFactory.getLogger(CustomerPointLogResource.class);
    private final CustomerService customerService;

    @GetMapping("/list")
    public ResponseEntity<List<CustomerPointLogEntity>> getListCustomerPointLog() {
        log.debug("REST request from user [{}] to list [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(customerService.getListPointLog());
    }
}
