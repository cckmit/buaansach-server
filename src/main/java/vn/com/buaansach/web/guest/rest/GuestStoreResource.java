package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestStoreService;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreDTO;

@RestController
@RequestMapping("/api/v1/guest/store")
@RequiredArgsConstructor
public class GuestStoreResource {
    private final String ENTITY_NAME = "guest-store";
    private final Logger log = LoggerFactory.getLogger(GuestStoreResource.class);
    private final GuestStoreService guestStoreService;

    @GetMapping("/get/{storeGuid}")
    public ResponseEntity<GuestStoreDTO> getStore(@PathVariable String storeGuid){
        log.debug("REST request from user [{}] to get {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(guestStoreService.getStore(storeGuid));
    }
}
