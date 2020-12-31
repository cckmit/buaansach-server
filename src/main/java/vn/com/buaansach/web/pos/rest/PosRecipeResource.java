package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.entity.common.RecipeEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosRecipeService;

import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/recipe")
@RequiredArgsConstructor
public class PosRecipeResource {
    private static final String ENTITY_NAME = "pos-recipe";
    private final Logger log = LoggerFactory.getLogger(PosRecipeResource.class);
    private final PosRecipeService posRecipeService;

    @GetMapping("/list")
    public ResponseEntity<List<RecipeEntity>> getRecipes() {
        log.debug("REST request from user [{}] to list all [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(posRecipeService.getRecipes());
    }
}
