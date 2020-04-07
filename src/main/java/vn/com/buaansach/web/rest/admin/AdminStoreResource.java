package vn.com.buaansach.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.model.entity.StoreEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.admin.AdminStoreService;
import vn.com.buaansach.model.dto.StoreDTO;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/store")
public class AdminStoreResource {
    private final String ENTITY_NAME = "store";
    private final Logger log = LoggerFactory.getLogger(AdminStoreResource.class);

    private final AdminStoreService adminStoreService;

    public AdminStoreResource(AdminStoreService adminStoreService) {
        this.adminStoreService = adminStoreService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StoreDTO> createStore(@Valid @RequestPart("payload") StoreEntity payload,
                                                @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(new StoreDTO(adminStoreService.createStore(payload, image)));
    }

    @PutMapping("/update")
    public ResponseEntity<StoreEntity> updateStore(@Valid @RequestPart("payload") StoreEntity payload,
                                                   @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user {} to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreService.updateStore(payload, image));
    }

    @DeleteMapping("/delete/{storeGuid}")
    public ResponseEntity<Void> delete(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        adminStoreService.deleteStore(storeGuid);
        return ResponseEntity.noContent().build();
    }
}
