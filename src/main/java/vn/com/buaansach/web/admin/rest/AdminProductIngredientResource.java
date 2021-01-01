package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.common.ProductIngredientEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminProductIngredientService;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminProductIngredientDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/product-ingredient")
@RequiredArgsConstructor
public class AdminProductIngredientResource {
    private static final String ENTITY_NAME = "admin-product-ingredient";
    private final Logger log = LoggerFactory.getLogger(AdminProductIngredientResource.class);
    private final AdminProductIngredientService adminProductIngredientService;

    @PostMapping("/create-by-product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createByProduct(@Valid @RequestBody AdminProductIngredientDTO payload) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminProductIngredientService.createByProduct(payload);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-by-ingredient")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createByIngredient(@Valid @RequestBody AdminProductIngredientDTO payload) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminProductIngredientService.createByIngredient(payload);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list-by-product/{productGuid}")
    public ResponseEntity<List<ProductIngredientEntity>> getByProduct(@PathVariable UUID productGuid) {
        log.debug("REST request from user [{}] to list [{}] by product [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, productGuid);
        return ResponseEntity.ok(adminProductIngredientService.getProductIngredientByProductGuid(productGuid));
    }

    @GetMapping("/list-by-ingredient/{ingredientGuid}")
    public ResponseEntity<List<ProductIngredientEntity>> getByIngredient(@PathVariable UUID ingredientGuid) {
        log.debug("REST request from user [{}] to list [{}] by ingredient [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, ingredientGuid);
        return ResponseEntity.ok(adminProductIngredientService.getProductIngredientByIngredientGuid(ingredientGuid));
    }

}
