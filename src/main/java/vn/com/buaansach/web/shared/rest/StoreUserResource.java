package vn.com.buaansach.web.shared.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.shared.service.StoreUserService;
import vn.com.buaansach.web.shared.service.dto.read.StoreUserDTO;

import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/store-user")
@RequiredArgsConstructor
public class StoreUserResource {
    private final String ENTITY_NAME = "store-user";
    private final Logger log = LoggerFactory.getLogger(StoreUserResource.class);
    private final StoreUserService storeUserService;

    @GetMapping("/list-by-user")
    public ResponseEntity<List<StoreUserDTO>> getListStoreUserByUser() {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to list [{}]", currentUser, ENTITY_NAME);
        return ResponseEntity.ok(storeUserService.getListStoreUserByUser(currentUser));
    }
}
