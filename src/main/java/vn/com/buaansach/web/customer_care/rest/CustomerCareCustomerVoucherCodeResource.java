package vn.com.buaansach.web.customer_care.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.customer_care.service.CustomerCareCustomerVoucherCodeService;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareCustomerVoucherCodeDTO;

import java.util.List;

@Secured(AuthoritiesConstants.CUSTOMER_CARE)
@RestController
@RequestMapping("/api/v1/customer-care/customer-voucher-code")
@RequiredArgsConstructor
public class CustomerCareCustomerVoucherCodeResource {
    private static final String ENTITY_NAME = "customer-care-customer-voucher-code";
    private final Logger log = LoggerFactory.getLogger(CustomerCareCustomerVoucherCodeResource.class);
    private final CustomerCareCustomerVoucherCodeService customerCareCustomerVoucherCodeService;

    @GetMapping("/list")
    public ResponseEntity<Page<CustomerCareCustomerVoucherCodeDTO>> getPageCustomer(@RequestParam(value = "search", defaultValue = "") String search,
                                                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                    @RequestParam(value = "size", defaultValue = "20") int size,
                                                                                    @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                                                    @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        log.debug("REST request from user [{}] to list [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, request);
        return ResponseEntity.ok(customerCareCustomerVoucherCodeService.getPageCustomer(request, search));
    }

    @GetMapping("/list-unsent-voucher")
    public ResponseEntity<List<CustomerCareCustomerVoucherCodeDTO>> getListUnsentVoucher() {
        log.debug("REST request from user [{}] to list unsent [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(customerCareCustomerVoucherCodeService.getListUnsentVoucher());
    }
}
