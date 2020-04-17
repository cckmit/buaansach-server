package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosProductService;
import vn.com.buaansach.web.pos.service.dto.PosProductDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pos/product")
public class PosProductResource {
    private final String ENTITY_NAME = "pos-area";
    private final Logger log = LoggerFactory.getLogger(PosAreaResource.class);
    private final PosProductService posProductService;

    public PosProductResource(PosProductService posProductService) {
        this.posProductService = posProductService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<PosProductDTO>> getListProduct() {
        log.debug("REST request from user {} to list {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(posProductService.getListProduct());
    }
}
