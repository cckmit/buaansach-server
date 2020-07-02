package vn.com.buaansach.web.customer_care.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.customer_care.service.CustomerCareOrderService;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareOrderDTO;

@Secured(AuthoritiesConstants.CUSTOMER_CARE)
@RestController
@RequestMapping("/api/v1/customer-care/order")
@RequiredArgsConstructor
public class CustomerCareOrderResource {
    private static final String ENTITY_NAME = "customer-care-order";
    private final Logger log = LoggerFactory.getLogger(CustomerCareOrderResource.class);
    private final CustomerCareOrderService customerCareOrderService;

    @GetMapping("/get/{orderGuid}")
    public ResponseEntity<CustomerCareOrderDTO> getOrderInfo(@PathVariable String orderGuid){
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        return ResponseEntity.ok(customerCareOrderService.getOrderInfo(orderGuid));
    }
}
