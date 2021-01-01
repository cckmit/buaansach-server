package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.common.IngredientEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosIngredientService;

import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/ingredient")
@RequiredArgsConstructor
public class PosIngredientResource {
    private static final String ENTITY_NAME = "pos-ingredient";
    private final Logger log = LoggerFactory.getLogger(PosIngredientResource.class);
    private final PosIngredientService posIngredientService;

    @GetMapping("/list")
    public ResponseEntity<List<IngredientEntity>> getAll() {
        log.debug("REST request from user [{}] to list all [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(posIngredientService.getIngredients());
    }
}
