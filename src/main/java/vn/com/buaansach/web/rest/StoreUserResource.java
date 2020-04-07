package vn.com.buaansach.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.service.StoreUserService;
import vn.com.buaansach.model.dto.StoreUserDTO;
import vn.com.buaansach.model.dto.request.AddStoreUserRequest;
import vn.com.buaansach.model.dto.request.CreateOrUpdateStoreUserRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store-user")
public class StoreUserResource {
    private static final String ENTITY_NAME = "store-user";
    private final Logger log = LoggerFactory.getLogger(StoreUserResource.class);
    private final StoreUserService storeUserService;

    public StoreUserResource(StoreUserService storeUserService) {
        this.storeUserService = storeUserService;
    }

    @PostMapping("/create")
    public ResponseEntity<StoreUserDTO> createStoreUser(@Valid @RequestBody CreateOrUpdateStoreUserRequest request) {
        log.debug("REST request to create {} : {}", ENTITY_NAME, request);
        return ResponseEntity.ok(storeUserService.createStoreUser(request));
    }

    @PostMapping("/add")
    public ResponseEntity<StoreUserDTO> addStoreUser(@Valid @RequestBody AddStoreUserRequest request) {
        log.debug("REST request to add {} : {}", ENTITY_NAME, request);
        return ResponseEntity.ok(storeUserService.addStoreUser(request));
    }

    /**
     * Activate or Deactivate an account managed by Admin, Store Owner or Store Manager
     * */
    @PutMapping("/toggle-account/{storeUserGuid}")
    public ResponseEntity<Void> toggleAccount(@PathVariable String storeUserGuid){
        log.debug("REST request to toggle account of {} by guid : {}", ENTITY_NAME, storeUserGuid);
        storeUserService.toggleAccount(storeUserGuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<StoreUserDTO> updateStoreUser(@Valid @RequestBody CreateOrUpdateStoreUserRequest request) {
        log.debug("REST request to update {} : {}", ENTITY_NAME, request);
        return ResponseEntity.ok(storeUserService.updateStoreUser(request));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<StoreUserDTO>> getListStoreUserByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request to get list {} by store guid : {}", ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(storeUserService.getListStoreUserByStoreGuid(storeGuid));
    }

}
