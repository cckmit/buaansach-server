package vn.com.buaansach.web.manager.rest;

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
import vn.com.buaansach.web.manager.service.ManagerCustomerService;
import vn.com.buaansach.web.manager.service.dto.ManagerUpdateCustomerDTO;

@Secured(AuthoritiesConstants.MANAGER)
@RestController
@RequestMapping("/api/v1/manager/customer")
@RequiredArgsConstructor
public class ManagerCustomerResource {
    private static final String ENTITY_NAME = "manager-customer";
    private final Logger log = LoggerFactory.getLogger(ManagerCustomerResource.class);
    private final ManagerCustomerService managerCustomerService;

    @PutMapping("/update-customer")
    public ResponseEntity<Void> updateCustomer(@RequestBody ManagerUpdateCustomerDTO payload) {
        log.debug("REST request from user [{}] to update [{}]  : [{}] ", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        managerCustomerService.updateCustomer(payload);
        return ResponseEntity.ok().build();
    }
}
