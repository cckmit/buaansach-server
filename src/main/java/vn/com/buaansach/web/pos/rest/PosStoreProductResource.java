package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosStoreProductService;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreProductStatusChangeDTO;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/store-product")
@RequiredArgsConstructor
public class PosStoreProductResource {
    private final String ENTITY_NAME = "pos-store-product";
    private final Logger log = LoggerFactory.getLogger(PosStoreProductResource.class);
    private final PosStoreProductService posStoreProductService;

    @PutMapping("/change-status")
    public ResponseEntity<Void> changeStoreProductStatus(@RequestBody PosStoreProductStatusChangeDTO payload) {
        log.debug("REST request from user [{}] to change status for [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        posStoreProductService.changeStoreProductStatus(payload);
        return ResponseEntity.ok().build();
    }
}
