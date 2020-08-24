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
import vn.com.buaansach.web.pos.service.PosAreaService;
import vn.com.buaansach.web.pos.service.dto.read.PosAreaDTO;

import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/area")
@RequiredArgsConstructor
public class PosAreaResource {
    private final String ENTITY_NAME = "pos-area";
    private final Logger log = LoggerFactory.getLogger(PosAreaResource.class);
    private final PosAreaService posAreaService;

    @GetMapping("/list-without-seat-by-store/{storeGuid}")
    public ResponseEntity<List<PosAreaDTO>> getListAreaWithoutSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list [{}] without seat by store: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posAreaService.getListAreaWithoutSeatByStoreGuid(storeGuid));
    }

    @GetMapping("/list-with-seat-by-store/{storeGuid}")
    public ResponseEntity<List<PosAreaDTO>> getListAreaWithSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list [{}] with seat by store: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posAreaService.getListAreaWithSeatByStoreGuid(storeGuid));
    }

}
