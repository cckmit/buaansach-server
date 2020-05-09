package vn.com.buaansach.web.admin.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminAreaService;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminAreaDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateAreaDTO;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/area")
public class AdminAreaResource {
    private static final String ENTITY_NAME = "admin-area";
    private final Logger log = LoggerFactory.getLogger(AdminAreaResource.class);

    private final AdminAreaService adminAreaService;

    public AdminAreaResource(AdminAreaService adminAreaService) {
        this.adminAreaService = adminAreaService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdminAreaDTO> createArea(@Valid @RequestBody AdminCreateAreaDTO payload) {
        log.debug("REST request from user [{}] to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminAreaService.createArea(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<AdminAreaDTO> updateArea(@Valid @RequestBody AdminAreaDTO payload) {
        log.debug("REST request from user [{}] to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminAreaService.updateArea(payload));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<AdminAreaDTO>> getListAreaByStore(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list {} by store : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminAreaService.getListAreaByStore(storeGuid));
    }

    @DeleteMapping("/delete/{areaGuid}")
    public ResponseEntity<Void> deleteArea(@PathVariable String areaGuid) {
        log.debug("REST request from user [{}] to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, areaGuid);
        adminAreaService.deleteArea(areaGuid);
        return ResponseEntity.noContent().build();
    }
}
