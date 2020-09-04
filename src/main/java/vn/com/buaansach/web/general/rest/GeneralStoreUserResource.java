package vn.com.buaansach.web.general.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.general.service.StoreUserService;
import vn.com.buaansach.web.general.service.dto.read.StoreUserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store-user")
@RequiredArgsConstructor
public class GeneralStoreUserResource {
    private final String ENTITY_NAME = "store-user";
    private final Logger log = LoggerFactory.getLogger(GeneralStoreUserResource.class);
    private final StoreUserService storeUserService;

    @GetMapping("/list-by-user")
    public ResponseEntity<List<StoreUserDTO>> getListStoreUserByUser() {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to list [{}]", currentUser, ENTITY_NAME);
        return ResponseEntity.ok(storeUserService.getListStoreUserByUser(currentUser));
    }
}
