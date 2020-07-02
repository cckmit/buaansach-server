package vn.com.buaansach.web.customer_care.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.customer_care.service.CustomerCareCustomerOrderService;
import vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareCustomerOrderDTO;
import vn.com.buaansach.web.customer_care.service.dto.write.CustomerCareCustomerOrderParamsDTO;

import java.util.List;

@Secured(AuthoritiesConstants.CUSTOMER_CARE)
@RestController
@RequestMapping("/api/v1/customer-care/customer-order")
@RequiredArgsConstructor
public class CustomerCareCustomerOrderResource {
    private static final String ENTITY_NAME = "customer-care-customer-order";
    private final Logger log = LoggerFactory.getLogger(CustomerCareCustomerOrderResource.class);
    private final CustomerCareCustomerOrderService customerCareCustomerOrderService;

    @PostMapping("/list-with-filter")
    public ResponseEntity<List<CustomerCareCustomerOrderDTO>> getListCustomerOrder(@RequestBody CustomerCareCustomerOrderParamsDTO payload){
        log.debug("REST request from user [{}] to list [{}] with filter  : [{}] ", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(customerCareCustomerOrderService.getListCustomerOrder(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateCustomerOrder(@RequestBody CustomerCareCustomerOrderDTO payload){
        log.debug("REST request from user [{}] to update [{}] : [{}] ", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        customerCareCustomerOrderService.updateCustomerOrder(payload);
        return ResponseEntity.ok().build();
    }

}
