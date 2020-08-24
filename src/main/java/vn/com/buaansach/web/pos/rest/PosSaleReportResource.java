package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosSaleReportService;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleReportParams;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;

import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/sale-report")
@RequiredArgsConstructor
public class PosSaleReportResource {
    private final String ENTITY_NAME = "pos-sale-report";
    private final Logger log = LoggerFactory.getLogger(PosSaleReportResource.class);
    private final PosSaleReportService posSaleReportService;

    @PostMapping("/current-user")
    public ResponseEntity<List<PosOrderDTO>> getCurrentUserReport(@RequestBody PosSaleReportParams payload) {
        log.debug("REST request from user [{}] to get [{}] for current user : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(posSaleReportService.getCurrentUserSaleReport(payload));
    }

    @PostMapping("/get-report")
    public ResponseEntity<List<PosOrderDTO>> getReport(@RequestBody PosSaleReportParams payload) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(posSaleReportService.getSaleReport(payload));
    }
}
