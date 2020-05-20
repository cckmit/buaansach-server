package vn.com.buaansach.web.customer_care.rest;

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
import vn.com.buaansach.web.customer_care.service.CustomerCareCustomerService;
import vn.com.buaansach.web.customer_care.service.dto.CustomerCareUpdateCustomerDTO;

@Secured(AuthoritiesConstants.CUSTOMER_CARE)
@RestController
@RequestMapping("/api/v1/customer-care/customer")
@RequiredArgsConstructor
public class CustomerCareCustomerResource {
    private static final String ENTITY_NAME = "customer-care-customer";
    private final Logger log = LoggerFactory.getLogger(CustomerCareCustomerResource.class);
    private final CustomerCareCustomerService customerCareCustomerService;

    @PutMapping("/update-customer")
    public ResponseEntity<Void> updateCustomer(@RequestBody CustomerCareUpdateCustomerDTO payload) {
        log.debug("REST request from user [{}] to update [{}]  : [{}] ", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        customerCareCustomerService.updateCustomer(payload);
        return ResponseEntity.ok().build();
    }
}