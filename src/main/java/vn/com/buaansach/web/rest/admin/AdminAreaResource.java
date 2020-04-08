package vn.com.buaansach.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.service.dto.AreaDTO;
import vn.com.buaansach.service.dto.manipulation.CreateAreaDTO;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.admin.AdminAreaService;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/area")
public class AdminAreaResource {
    private static final String ENTITY_NAME = "area";
    private final Logger log = LoggerFactory.getLogger(AdminAreaResource.class);

    private final AdminAreaService adminAreaService;

    public AdminAreaResource(AdminAreaService adminAreaService) {
        this.adminAreaService = adminAreaService;
    }

    @PostMapping("/create")
    public ResponseEntity<AreaDTO> createArea(@Valid @RequestBody CreateAreaDTO payload) {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminAreaService.createArea(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<AreaDTO> updateArea(@Valid @RequestBody AreaEntity payload) {
        log.debug("REST request from user {} to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminAreaService.updateArea(payload));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<AreaDTO>> getListAreaByStore(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to list {} by store : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminAreaService.getListAreaByStore(storeGuid));
    }

    @DeleteMapping("/delete/{areaGuid}")
    public ResponseEntity<Void> deleteArea(@PathVariable String areaGuid) {
        log.debug("REST request from user {} to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, areaGuid);
        adminAreaService.deleteArea(areaGuid);
        return ResponseEntity.noContent().build();
    }
}
