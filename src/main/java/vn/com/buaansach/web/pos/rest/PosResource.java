package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosService;
import vn.com.buaansach.web.pos.service.dto.PosAreaDTO;
import vn.com.buaansach.web.pos.service.dto.PosSeatDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pos")
public class PosResource {
    private final Logger log = LoggerFactory.getLogger(PosResource.class);
    private final PosService posService;

    public PosResource(PosService posService) {
        this.posService = posService;
    }

    @GetMapping("/list-seat-by-store/{storeGuid}")
    public ResponseEntity<List<PosSeatDTO>> getListSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to get list seat by store: {}", SecurityUtils.getCurrentUserLogin(), storeGuid);
        return ResponseEntity.ok(posService.getListSeatByStoreGuid(storeGuid));
    }

    @GetMapping("/list-area-with-seat-by-store/{storeGuid}")
    public ResponseEntity<List<PosAreaDTO>> getListAreaWithSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to get list area with seat by store: {}", SecurityUtils.getCurrentUserLogin(), storeGuid);
        return ResponseEntity.ok(posService.getListAreaWithSeatByStoreGuid(storeGuid));
    }

}
