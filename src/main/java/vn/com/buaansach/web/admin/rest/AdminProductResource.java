package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
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
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminProductService;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminProductDTO;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/product")
@RequiredArgsConstructor
public class AdminProductResource {
    private static final String ENTITY_NAME = "admin-product";
    private final Logger log = LoggerFactory.getLogger(AdminProductResource.class);
    private final AdminProductService adminProductService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdminProductDTO> createProduct(@Valid @RequestPart("payload") AdminProductDTO payload,
                                                         @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminProductService.createProduct(payload, image));
    }

    @PutMapping("/update")
    public ResponseEntity<AdminProductDTO> updateProduct(@Valid @RequestPart("payload") AdminProductDTO payload,
                                                         @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminProductService.updateProduct(payload, image));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<AdminProductDTO>> getPageProduct(@RequestParam(value = "search", defaultValue = "") String search,
                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "20") int size,
                                                                @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                                @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        log.debug("REST request from user [{}] to list [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, request);
        return ResponseEntity.ok(adminProductService.getPageProduct(request, search));
    }

    @GetMapping("/list-not-in-store/{storeGuid}")
    public ResponseEntity<List<ProductEntity>> getListProductNotInStore(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list [{}] not in store : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminProductService.getListProductNotInStore(storeGuid));
    }

    @GetMapping("/get/{productGuid}")
    public ResponseEntity<AdminProductDTO> getProduct(@PathVariable String productGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, productGuid);
        return ResponseEntity.ok(adminProductService.getProduct(productGuid));
    }

    @DeleteMapping("/delete/{productGuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productGuid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, productGuid);
        adminProductService.deleteProduct(productGuid);
        return ResponseEntity.noContent().build();
    }
}
