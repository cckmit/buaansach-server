package vn.com.buaansach.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.model.entity.SeatEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.service.SeatService;
import vn.com.buaansach.model.dto.request.CreateSeatRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/seat")
public class SeatResource {
    private static final String ENTITY_NAME = "seat";
    private final Logger log = LoggerFactory.getLogger(SeatResource.class);

    private final SeatService seatService;

    public SeatResource(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping("/create")
    public ResponseEntity<SeatEntity> createSeat(@Valid @RequestBody CreateSeatRequest entity) {
        log.debug("REST request to create {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(seatService.createSeat(entity));
    }

    @PutMapping("/update")
    public ResponseEntity<SeatEntity> updateSeat(@Valid @RequestBody SeatEntity entity) {
        if (entity.getGuid() == null) throw new BadRequestException("error.update.idNull");
        log.debug("REST request to update {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(seatService.updateSeat(entity));
    }

    @GetMapping("/list-by-area/{areaGuid}")
    public ResponseEntity<List<SeatEntity>> getListSeatByAreaGuid(@PathVariable String areaGuid) {
        log.debug("REST request to list {} with area guid : {}", ENTITY_NAME, areaGuid);
        return ResponseEntity.ok(seatService.getListSeatByAreaGuid(areaGuid));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<SeatEntity>> getListSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request to list {} with store guid : {}", ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(seatService.getListSeatByStoreGuid(storeGuid));
    }

    @DeleteMapping("/delete/{seatGuid}")
    public ResponseEntity<Void> deleteSeat(@PathVariable String seatGuid) {
        log.debug("REST request to delete {} with guid : {}", ENTITY_NAME, seatGuid);
        seatService.deleteSeat(seatGuid);
        return ResponseEntity.noContent().build();
    }
}
