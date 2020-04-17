package vn.com.buaansach.web.admin.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminStoreService;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/store")
public class AdminStoreResource {
    private final String ENTITY_NAME = "admin-store";
    private final Logger log = LoggerFactory.getLogger(AdminStoreResource.class);

    private final AdminStoreService adminStoreService;

    public AdminStoreResource(AdminStoreService adminStoreService) {
        this.adminStoreService = adminStoreService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StoreEntity> createStore(@Valid @RequestPart("payload") StoreEntity payload,
                                                   @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreService.createStore(payload, image));
    }

    @PutMapping("/update")
    public ResponseEntity<StoreEntity> updateStore(@Valid @RequestPart("payload") StoreEntity payload,
                                                   @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user {} to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreService.updateStore(payload, image));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<StoreEntity>> getPageStore(@RequestParam(value = "search", defaultValue = "") String search,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "20") int size,
                                                          @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                          @RequestParam(value = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection) {
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        log.debug("REST request from user {} to list {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, request);
        return ResponseEntity.ok(adminStoreService.getPageStore(request, search));
    }

    @GetMapping("/get/{storeGuid}")
    public ResponseEntity<StoreEntity> getOneStore(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to get {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminStoreService.getOneStore(storeGuid));
    }

    @DeleteMapping("/delete/{storeGuid}")
    public ResponseEntity<Void> deleteStore(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        adminStoreService.deleteStore(storeGuid);
        return ResponseEntity.noContent().build();
    }
}
