package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminSaleService;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/sale")
@RequiredArgsConstructor
public class AdminSaleResource {
    private final String ENTITY_NAME = "admin-sale";
    private final Logger log = LoggerFactory.getLogger(AdminUserResource.class);
    private final AdminSaleService adminSaleService;

    @PostMapping("/create")
    public ResponseEntity<AdminSaleDTO> createSale(@Valid @RequestPart("payload") AdminSaleDTO payload,
                                                   @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminSaleService.createSale(payload, image));
    }

    @GetMapping("/get/{saleGuid}")
    public ResponseEntity<AdminSaleDTO> getSale(@PathVariable UUID saleGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, saleGuid);
        return ResponseEntity.ok(adminSaleService.getSale(saleGuid));
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminSaleDTO>> getListSale() {
        log.debug("REST request from user [{}] to list [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminSaleService.getListSale());
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<AdminSaleDTO>> getListSaleByStore(@PathVariable UUID storeGuid) {
        log.debug("REST request from user [{}] to list [{}]: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminSaleService.getListAdminSaleDTOByStoreGuid(storeGuid));
    }

    @PutMapping("/update")
    public ResponseEntity<AdminSaleDTO> updateSale(@Valid @RequestPart("payload") AdminSaleDTO payload,
                                                   @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminSaleService.updateSale(payload, image));
    }

    @DeleteMapping("/delete/{saleGuid}")
    public ResponseEntity<Void> deleteSale(@PathVariable UUID saleGuid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, saleGuid);
        adminSaleService.deleteSale(saleGuid);
        return ResponseEntity.noContent().build();
    }

}
