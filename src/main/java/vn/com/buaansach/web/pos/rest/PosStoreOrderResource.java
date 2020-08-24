package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosStoreOrderService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreOrderDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreOrderStatusUpdateDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreOrderVisibilityUpdateDTO;

import java.time.Instant;
import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/store-order")
@RequiredArgsConstructor
public class PosStoreOrderResource {
    private final String ENTITY_NAME = "pos-store-order";
    private final Logger log = LoggerFactory.getLogger(PosStoreOrderResource.class);
    private final PosStoreOrderService posStoreOrderService;

    @GetMapping("/list-by-store")
    public ResponseEntity<List<PosStoreOrderDTO>> getListStoreOrder(@RequestParam("storeGuid") String storeGuid,
                                                                    @RequestParam("startDate") Instant startDate,
                                                                    @RequestParam(value = "hidden", required = false) Boolean hidden) {
        log.debug("REST request from user [{}] to list [{}] by store: [{}] - from [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid, startDate);
        return ResponseEntity.ok(posStoreOrderService.getListStoreOrder(storeGuid, startDate, hidden));
    }

    @PutMapping("/update-status")
    public ResponseEntity<PosStoreOrderDTO> updateStoreOrder(@RequestBody PosStoreOrderStatusUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to update [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posStoreOrderService.updateStoreOrder(payload, currentUser));
    }

    @PutMapping("/toggle-visibility")
    public ResponseEntity<Void> toggleVisibility(@RequestBody PosStoreOrderVisibilityUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to toggle [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posStoreOrderService.toggleVisibility(payload, currentUser);
        return ResponseEntity.ok().build();
    }
}
