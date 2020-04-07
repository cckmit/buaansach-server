package vn.com.buaansach.web.rest.admin;

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
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.admin.ProductManagementService;

import javax.validation.Valid;
import java.net.URISyntaxException;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/management/product")
public class ProductManagementResource {
    private static final String ENTITY_NAME = "product";
    private final Logger log = LoggerFactory.getLogger(ProductManagementResource.class);
    private final ProductManagementService productManagementService;

    public ProductManagementResource(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductEntity> createProduct(@Valid @RequestPart("entity") ProductEntity entity,
                                                       @RequestPart(value = "image", required = false) MultipartFile image) throws URISyntaxException {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, entity);
        return ResponseEntity.ok(productManagementService.createProduct(entity, image));
    }

    @PutMapping("/update")
    public ResponseEntity<ProductEntity> updateProduct(@Valid @RequestPart("entity") ProductEntity entity,
                                                       @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user {} to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, entity);
        return ResponseEntity.ok(productManagementService.updateProduct(entity, image));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProductEntity>> getPageProduct(@RequestParam(value = "search", defaultValue = "") String search,
                                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "size", defaultValue = "20") int size,
                                                              @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                              @RequestParam(value = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection) {
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        log.debug("REST request from user {} to get page {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, request);
        return ResponseEntity.ok(productManagementService.getPageProduct(request, search));
    }

    @DeleteMapping("/delete/{productGuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productGuid) {
        log.debug("REST request from user {} to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, productGuid);
        productManagementService.deleteProduct(productGuid);
        return ResponseEntity.noContent().build();
    }
}
