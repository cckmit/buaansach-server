package vn.com.buaansach.web.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.common.service.StoreUserService;
import vn.com.buaansach.web.common.service.dto.read.StoreUserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store-user")
public class CommonStoreUserResource {
    private final String ENTITY_NAME = "store-user";
    private final Logger log = LoggerFactory.getLogger(CommonStoreUserResource.class);
    private final StoreUserService storeUserService;

    public CommonStoreUserResource(StoreUserService storeUserService) {
        this.storeUserService = storeUserService;
    }

    @GetMapping("/list-by-user")
    public ResponseEntity<List<StoreUserDTO>> getListStoreUserByUser() {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to list [{}]", currentUser, ENTITY_NAME);
        return ResponseEntity.ok(storeUserService.getListStoreUserByUser(currentUser));
    }
}
