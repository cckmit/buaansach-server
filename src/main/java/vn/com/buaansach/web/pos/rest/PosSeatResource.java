package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosSeatService;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pos/seat")
public class PosSeatResource {
    private final String ENTITY_NAME = "pos-seat";
    private final Logger log = LoggerFactory.getLogger(PosSeatResource.class);
    private final PosSeatService posSeatService;

    public PosSeatResource(PosSeatService posSeatService) {
        this.posSeatService = posSeatService;
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<PosSeatDTO>> getListSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list {} by store: {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posSeatService.getListSeatByStoreGuid(storeGuid));
    }

    @GetMapping("/get/{seatGuid}")
    public ResponseEntity<PosSeatDTO> getSeat(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to get {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(posSeatService.getPosSeatDTO(seatGuid));
    }
}
