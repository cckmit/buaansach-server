package vn.com.buaansach.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.service.AreaService;
import vn.com.buaansach.service.dto.AreaDTO;
import vn.com.buaansach.service.request.CreateAreaRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/area")
public class AreaResource {
    private static final String ENTITY_NAME = "area";
    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private final AreaService areaService;

    public AreaResource(AreaService areaService) {
        this.areaService = areaService;
    }

    @PostMapping("/create")
    public ResponseEntity<AreaDTO> createArea(@Valid @RequestBody CreateAreaRequest request) {
        log.debug("REST request to create {} : {}", ENTITY_NAME, request);
        return ResponseEntity.ok(areaService.createArea(request));
    }

    @PutMapping("/update")
    public ResponseEntity<AreaDTO> updateArea(@Valid @RequestBody AreaEntity entity) {
        if (entity.getGuid() == null) throw new BadRequestException("error.update.idNull");
        log.debug("REST request to update {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(areaService.updateArea(entity));
    }

    @GetMapping("/list/{storeGuid}")
    public ResponseEntity<List<AreaDTO>> getListAreaByStore(@PathVariable String storeGuid) {
        log.debug("REST request to list {} with store guid : {}", ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(areaService.getListAreaByStore(storeGuid));
    }

    @DeleteMapping("/delete/{areaGuid}")
    public ResponseEntity<Void> deleteArea(@PathVariable String areaGuid) {
        log.debug("REST request to delete {} with guid : {}", ENTITY_NAME, areaGuid);
        areaService.deleteArea(areaGuid);
        return ResponseEntity.noContent().build();
    }
}
