package vn.com.buaansach.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.service.SeatService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/seat")
public class SeatResource {
    private static final String ENTITY_NAME = "seat";
    private final Logger log = LoggerFactory.getLogger(SeatResource.class);

    private final SeatService seatService;

    public SeatResource(SeatService seatService) {
        this.seatService = seatService;
    }

    @Secured(AuthoritiesConstants.MANAGER)
    @PostMapping("/create")
    public ResponseEntity<SeatEntity> create(@Valid @RequestBody SeatEntity entity) {
        if (entity.getId() != null || entity.getGuid() != null) throw new BadRequestException("error.create.idExists");
        log.debug("REST request to create {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(seatService.create(entity));
    }

    @Secured(AuthoritiesConstants.MANAGER)
    @PutMapping("/update")
    public ResponseEntity<SeatEntity> update(@Valid @RequestBody SeatEntity entity) {
        if (entity.getGuid() == null) throw new BadRequestException("error.update.idNull");
        log.debug("REST request to update {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(seatService.update(entity));
    }

    @Secured(AuthoritiesConstants.MANAGER)
    @GetMapping("/list/{areaGuid}")
    public ResponseEntity<List<SeatEntity>> getListByArea(@PathVariable String areaGuid) {
        log.debug("REST request to list {} with area guid : {}", ENTITY_NAME, areaGuid);
        return ResponseEntity.ok(seatService.getListByArea(areaGuid));
    }

    @Secured(AuthoritiesConstants.MANAGER)
    @DeleteMapping("/delete/{seatGuid}")
    public ResponseEntity<Void> delete(@PathVariable String seatGuid) {
        log.debug("REST request to delete {} with guid : {}", ENTITY_NAME, seatGuid);
        seatService.delete(seatGuid);
        return ResponseEntity.noContent().build();
    }
}
