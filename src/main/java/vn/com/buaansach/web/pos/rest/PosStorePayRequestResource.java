package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosStorePayRequestService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStorePayRequestDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStorePayRequestStatusUpdateDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStorePayRequestVisibilityUpdateDTO;

import java.time.Instant;
import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/store-pay-request")
@RequiredArgsConstructor
public class PosStorePayRequestResource {
    private final String ENTITY_NAME = "store-pay-request";
    private final Logger log = LoggerFactory.getLogger(PosStorePayRequestResource.class);
    private final PosStorePayRequestService posStorePayRequestService;

    @GetMapping("/list-by-store")
    public ResponseEntity<List<PosStorePayRequestDTO>> getListStorePayRequest(@RequestParam("storeGuid") String storeGuid,
                                                                    @RequestParam("startDate") Instant startDate,
                                                                    @RequestParam(value = "hidden", required = false) Boolean hidden) {
        log.debug("REST request from user [{}] to list [{}] by store: [{}] - from [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid, startDate);
        return ResponseEntity.ok(posStorePayRequestService.getListStorePayRequest(storeGuid, startDate, hidden));
    }

    @PutMapping("/update-status")
    public ResponseEntity<PosStorePayRequestDTO> updateStorePayRequest(@RequestBody PosStorePayRequestStatusUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to update [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posStorePayRequestService.updateStorePayRequest(payload, currentUser));
    }

    @PutMapping("/toggle-visibility")
    public ResponseEntity<Void> toggleVisibility(@RequestBody PosStorePayRequestVisibilityUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to toggle [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posStorePayRequestService.toggleVisibility(payload, currentUser);
        return ResponseEntity.ok().build();
    }
}
