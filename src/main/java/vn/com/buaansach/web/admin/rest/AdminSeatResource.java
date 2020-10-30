package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminSeatService;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateSeatDTO;

import javax.validation.Valid;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/seat")
@RequiredArgsConstructor
public class AdminSeatResource {
    private static final String ENTITY_NAME = "admin-seat";
    private final Logger log = LoggerFactory.getLogger(AdminSeatResource.class);

    private final AdminSeatService adminSeatService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SeatEntity> createSeat(@Valid @RequestBody AdminCreateSeatDTO payload) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminSeatService.createSeat(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<SeatEntity> updateSeat(@Valid @RequestBody SeatEntity payload) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminSeatService.updateSeat(payload));
    }

    @DeleteMapping("/delete/{seatGuid}")
    public ResponseEntity<Void> deleteSeat(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        adminSeatService.deleteSeat(UUID.fromString(seatGuid));
        return ResponseEntity.noContent().build();
    }
}
