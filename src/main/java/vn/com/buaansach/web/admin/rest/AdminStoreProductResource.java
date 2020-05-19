package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminStoreProductService;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/store-product")
@RequiredArgsConstructor
public class AdminStoreProductResource {
    private final String ENTITY_NAME = "admin-store-product";
    private final Logger log = LoggerFactory.getLogger(AdminStoreProductResource.class);
    private final AdminStoreProductService adminStoreProductService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdminStoreProductDTO> addProductToStore(@Valid @RequestBody AdminStoreProductDTO payload) {
        log.debug("REST request from user [{}] to add {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreProductService.addProductToStore(payload));
    }

    @PostMapping("/add-all")
    public ResponseEntity<List<AdminStoreProductDTO>> addAllProductToStore(@RequestBody String storeGuid) {
        log.debug("REST request from user [{}] to add all {} to store : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminStoreProductService.addAllProductToStore(storeGuid));
    }

    @PutMapping("/update")
    public ResponseEntity<AdminStoreProductDTO> updateStoreProduct(@Valid @RequestBody AdminStoreProductDTO payload) {
        log.debug("REST request from user [{}] to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreProductService.updateStoreProduct(payload));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<AdminStoreProductDTO>> getListStoreProductByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list {} by store : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminStoreProductService.getListStoreProductByStoreGuid(storeGuid));
    }

    @DeleteMapping("/delete/{storeProductGuid}")
    public ResponseEntity<Void> deleteStoreProduct(@PathVariable String storeProductGuid) {
        log.debug("REST request from user [{}] to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeProductGuid);
        adminStoreProductService.deleteStoreProduct(storeProductGuid);
        return ResponseEntity.noContent().build();
    }
}
