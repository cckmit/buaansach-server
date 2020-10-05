package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminStoreSaleService;
import vn.com.buaansach.web.admin.service.dto.write.AdminMakeSalePrimaryDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminStoreSaleDTO;

import java.util.List;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/store-sale")
@RequiredArgsConstructor
public class AdminStoreSaleResource {
    private final String ENTITY_NAME = "admin-store-sale";
    private final Logger log = LoggerFactory.getLogger(AdminUserResource.class);
    private final AdminStoreSaleService adminStoreSaleService;

    @PostMapping("/add")
    public ResponseEntity<Void> addStoreSale(@RequestBody List<AdminStoreSaleDTO> payload) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminStoreSaleService.addStoreSale(payload);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list-by-sale/{saleGuid}")
    public ResponseEntity<List<AdminStoreSaleDTO>> getListStoreSaleBySale(@PathVariable UUID saleGuid) {
        log.debug("REST request from user [{}] to list by sale [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, saleGuid);
        return ResponseEntity.ok(adminStoreSaleService.getListStoreSaleBySale(saleGuid));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateStoreSale(@RequestBody AdminStoreSaleDTO payload) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminStoreSaleService.updateStoreSale(payload);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{storeSaleGuid}")
    public ResponseEntity<Void> deleteStoreSale(@PathVariable UUID storeSaleGuid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeSaleGuid);
        adminStoreSaleService.deleteStoreSale(storeSaleGuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/make-primary")
    public ResponseEntity<Void> makePrimary(@RequestBody AdminMakeSalePrimaryDTO payload) {
        log.debug("REST request from user [{}] to make primary [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminStoreSaleService.makePrimary(payload);
        return ResponseEntity.noContent().build();
    }

}
