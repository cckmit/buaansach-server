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
import vn.com.buaansach.web.customer_care.service.CustomerCareVoucherCodeService;
import vn.com.buaansach.web.customer_care.service.dto.CustomerCareUpdateVoucherCodeDTO;

@Secured(AuthoritiesConstants.CUSTOMER_CARE)
@RestController
@RequestMapping("/api/v1/customer-care/voucher-code")
@RequiredArgsConstructor
public class CustomerCareVoucherCodeResource {
    private static final String ENTITY_NAME = "customer-care-voucher-code";
    private final Logger log = LoggerFactory.getLogger(CustomerCareVoucherCodeResource.class);
    private final CustomerCareVoucherCodeService customerCareVoucherCodeService;

    @PutMapping("/update-voucher-code")
    public ResponseEntity<Void> updateFirstRegVoucherCode(@RequestBody CustomerCareUpdateVoucherCodeDTO payload) {
        log.debug("REST request from user [{}] to update [{}]  : [{}] ", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        customerCareVoucherCodeService.updateFirstRegVoucherCode(payload);
        return ResponseEntity.ok().build();
    }
}
