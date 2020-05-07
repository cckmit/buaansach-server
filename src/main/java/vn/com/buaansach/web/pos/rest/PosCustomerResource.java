package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosCustomerService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosCustomerDTO;

@RestController
@RequestMapping("/api/v1/pos/customer")
public class PosCustomerResource {
    private final String ENTITY_NAME = "pos-customer";
    private final Logger log = LoggerFactory.getLogger(PosCustomerResource.class);
    private final PosCustomerService posCustomerService;

    public PosCustomerResource(PosCustomerService posCustomerService) {
        this.posCustomerService = posCustomerService;
    }

    @PostMapping("/create")
    public ResponseEntity<PosCustomerDTO> getCustomerByPhone(@RequestBody PosCustomerDTO payload) {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(posCustomerService.posCreateCustomer(payload));
    }

    @GetMapping("/get/{customerPhone}")
    public ResponseEntity<PosCustomerDTO> getCustomerByPhone(@PathVariable String customerPhone) {
        log.debug("REST request from user {} to list {} by phone : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, customerPhone);
        return ResponseEntity.ok(posCustomerService.getCustomerByPhone(customerPhone));
    }


}
