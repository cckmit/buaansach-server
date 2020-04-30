package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosStoreProductService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pos/store-product")
public class PosStoreProductResource {
    private final String ENTITY_NAME = "pos-store-product";
    private final Logger log = LoggerFactory.getLogger(PosStoreProductResource.class);
    private final PosStoreProductService posStoreProductService;

    public PosStoreProductResource(PosStoreProductService posStoreProductService) {
        this.posStoreProductService = posStoreProductService;
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<PosStoreProductDTO>> getListProductByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to list {} by store: {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posStoreProductService.getListProductByStoreGuid(storeGuid));
    }
}
