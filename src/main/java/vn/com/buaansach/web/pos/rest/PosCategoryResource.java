package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosCategoryService;
import vn.com.buaansach.web.pos.service.dto.read.PosCategoryDTO;

import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/category")
@RequiredArgsConstructor
public class PosCategoryResource {
    private final String ENTITY_NAME = "pos-category";
    private final Logger log = LoggerFactory.getLogger(PosCategoryResource.class);
    private final PosCategoryService posCategoryService;

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<PosCategoryDTO>> getListPosCategoryDTO(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(posCategoryService.getListPosCategoryDTO(storeGuid));
    }
}
