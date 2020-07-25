package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestCustomerService;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestCustomerDTO;

@RestController
@RequestMapping("/api/v1/guest/customer")
@RequiredArgsConstructor
public class GuestCustomerResource {
    private final String ENTITY_NAME = "guest-customer";
    private final Logger log = LoggerFactory.getLogger(GuestCustomerResource.class);
    private final GuestCustomerService guestCustomerService;

//    @PostMapping("/create")
//    public ResponseEntity<GuestCustomerDTO> guestCreateCustomer(@RequestBody GuestCustomerDTO payload) {
//        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
//        return ResponseEntity.ok(guestCustomerService.guestCreateCustomer(payload));
//    }
}
