package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosCategoryService;
import vn.com.buaansach.web.pos.service.dto.read.PosCategoryDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pos/category")
public class PosCategoryResource {
    private final String ENTITY_NAME = "pos-category";
    private final Logger log = LoggerFactory.getLogger(PosAreaResource.class);
    private final PosCategoryService posCategoryService;

    public PosCategoryResource(PosCategoryService posCategoryService) {
        this.posCategoryService = posCategoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<PosCategoryDTO>> getListPosCategoryDTO() {
        log.debug("REST request from user {} to list {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(posCategoryService.getListPosCategoryDTO().stream()
                .map(PosCategoryDTO::new)
                .collect(Collectors.toList()));
    }
}
