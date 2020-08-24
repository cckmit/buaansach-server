package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosSeatService;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/seat")
@RequiredArgsConstructor
public class PosSeatResource {
    private final String ENTITY_NAME = "pos-seat";
    private final Logger log = LoggerFactory.getLogger(PosSeatResource.class);
    private final PosSeatService posSeatService;

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<PosSeatDTO>> getListSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list [{}] by store: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posSeatService.getListSeatByStoreGuid(storeGuid));
    }

    @GetMapping("/get/{seatGuid}")
    public ResponseEntity<PosSeatDTO> getSeat(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(posSeatService.getPosSeatDTO(seatGuid));
    }

    @PutMapping("/toggle-lock/{seatGuid}")
    public ResponseEntity<Void> toggleLock(@PathVariable String seatGuid){
        log.debug("REST request from user [{}] to toggle lock [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        posSeatService.toggleLock(seatGuid);
        return ResponseEntity.ok().build();
    }
}
