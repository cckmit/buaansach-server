package vn.com.buaansach.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.service.AreaService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/area")
public class AreaResource {
    private static final String ENTITY_NAME = "area";
    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private final AreaService areaService;

    public AreaResource(AreaService areaService) {
        this.areaService = areaService;
    }

    @Secured(AuthoritiesConstants.MANAGER)
    @PostMapping("/create")
    public ResponseEntity<AreaEntity> create(@Valid @RequestBody AreaEntity entity) {
        if (entity.getId() != null || entity.getGuid() != null) throw new BadRequestException("error.create.idExists");
        log.debug("REST request to create {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(areaService.create(entity));
    }

    @Secured(AuthoritiesConstants.MANAGER)
    @PutMapping("/update")
    public ResponseEntity<AreaEntity> update(@Valid @RequestBody AreaEntity entity) {
        if (entity.getGuid() == null) throw new BadRequestException("error.update.idNull");
        log.debug("REST request to update {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(areaService.update(entity));
    }

    @Secured(AuthoritiesConstants.MANAGER)
    @GetMapping("/list/{storeGuid}")
    public ResponseEntity<List<AreaEntity>> getListByStore(@PathVariable String storeGuid) {
        log.debug("REST request to list {} with store guid : {}", ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(areaService.getListByStore(storeGuid));
    }

    @DeleteMapping("/delete/{areaGuid}")
    public ResponseEntity<Void> delete(@PathVariable String areaGuid) {
        log.debug("REST request to delete {} with guid : {}", ENTITY_NAME, areaGuid);
        areaService.delete(areaGuid);
        return ResponseEntity.noContent().build();
    }
}
