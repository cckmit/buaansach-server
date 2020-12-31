package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.RecipeEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminRecipeService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/recipe")
@RequiredArgsConstructor
public class AdminRecipeResource {
    private static final String ENTITY_NAME = "admin-recipe";
    private final Logger log = LoggerFactory.getLogger(AdminRecipeResource.class);
    private final AdminRecipeService adminRecipeService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RecipeEntity> createRecipe(@Valid @RequestPart("payload") RecipeEntity payload,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminRecipeService.createRecipe(payload, image));
    }

    @PutMapping("/update")
    public ResponseEntity<RecipeEntity> updateRecipe(@Valid @RequestPart("payload") RecipeEntity payload,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminRecipeService.updateRecipe(payload, image));
    }

    @GetMapping("/list")
    public ResponseEntity<List<RecipeEntity>> getRecipes() {
        log.debug("REST request from user [{}] to list all [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminRecipeService.getRecipes());
    }

    @DeleteMapping("/delete/{guid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID guid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, guid);
        adminRecipeService.deleteRecipe(guid);
        return ResponseEntity.noContent().build();
    }
}
