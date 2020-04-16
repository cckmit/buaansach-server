package vn.com.buaansach.web.admin.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminSeatService;
import vn.com.buaansach.web.admin.service.manipulation.AdminCreateSeatDTO;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/seat")
public class AdminSeatResource {
    private static final String ENTITY_NAME = "admin-seat";
    private final Logger log = LoggerFactory.getLogger(AdminSeatResource.class);

    private final AdminSeatService adminSeatService;

    public AdminSeatResource(AdminSeatService adminSeatService) {
        this.adminSeatService = adminSeatService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SeatEntity> createSeat(@Valid @RequestBody AdminCreateSeatDTO payload) {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminSeatService.createSeat(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<SeatEntity> updateSeat(@Valid @RequestBody SeatEntity payload) {
        log.debug("REST request from user {} to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminSeatService.updateSeat(payload));
    }

    @GetMapping("/list-by-area/{areaGuid}")
    public ResponseEntity<List<SeatEntity>> getListSeatByAreaGuid(@PathVariable String areaGuid) {
        log.debug("REST request from user {} to list {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, areaGuid);
        return ResponseEntity.ok(adminSeatService.getListSeatByAreaGuid(areaGuid));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<SeatEntity>> getListSeatByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to list {} by store : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminSeatService.getListSeatByStoreGuid(storeGuid));
    }

    @DeleteMapping("/delete/{seatGuid}")
    public ResponseEntity<Void> deleteSeat(@PathVariable String seatGuid) {
        log.debug("REST request from user {} to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        adminSeatService.deleteSeat(seatGuid);
        return ResponseEntity.noContent().build();
    }
}
