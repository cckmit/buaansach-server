package vn.com.buaansach.web.admin.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminCategoryService;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/category")
public class AdminCategoryResource {
    private static final String ENTITY_NAME = "admin-category";
    private final Logger log = LoggerFactory.getLogger(AdminCategoryResource.class);
    private final AdminCategoryService adminCategoryService;

    public AdminCategoryResource(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryEntity> createCategory(@Valid @RequestPart("payload") CategoryEntity payload,
                                                         @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminCategoryService.createCategory(payload, image));
    }

    @PutMapping("/update")
    public ResponseEntity<CategoryEntity> updateCategory(@Valid @RequestPart("payload") CategoryEntity payload,
                                                         @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminCategoryService.updateCategory(payload, image));
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryEntity>> getAllCategory() {
        log.debug("REST request from user [{}] to list all {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminCategoryService.getAllCategory());
    }

    @GetMapping("/get/{categoryGuid}")
    public ResponseEntity<CategoryEntity> getCategory(@PathVariable String categoryGuid) {
        log.debug("REST request from user [{}] to get {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, categoryGuid);
        return ResponseEntity.ok(adminCategoryService.getCategory(categoryGuid));
    }

    @DeleteMapping("/delete/{categoryGuid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryGuid) {
        log.debug("REST request from user [{}] to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, categoryGuid);
        adminCategoryService.deleteCategory(categoryGuid);
        return ResponseEntity.noContent().build();
    }
}
