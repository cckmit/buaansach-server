package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.IngredientEntity;
import vn.com.buaansach.entity.common.RecipeEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminIngredientService;
import vn.com.buaansach.web.admin.service.AdminRecipeService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/ingredient")
@RequiredArgsConstructor
public class AdminIngredientResource {
    private static final String ENTITY_NAME = "admin-ingredient";
    private final Logger log = LoggerFactory.getLogger(AdminIngredientResource.class);
    private final AdminIngredientService adminIngredientService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientEntity> create(@Valid @RequestPart("payload") IngredientEntity payload,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminIngredientService.createIngredient(payload, image));
    }

    @PutMapping("/update")
    public ResponseEntity<IngredientEntity> update(@Valid @RequestPart("payload") IngredientEntity payload,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminIngredientService.updateIngredient(payload, image));
    }

    @GetMapping("/list")
    public ResponseEntity<List<IngredientEntity>> getAll() {
        log.debug("REST request from user [{}] to list all [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminIngredientService.getIngredients());
    }

    @DeleteMapping("/delete/{guid}")
    public ResponseEntity<Void> delete(@PathVariable UUID guid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, guid);
        adminIngredientService.deleteIngredient(guid);
        return ResponseEntity.noContent().build();
    }
}
