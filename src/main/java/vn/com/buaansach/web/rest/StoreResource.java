package vn.com.buaansach.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.service.StoreService;
import vn.com.buaansach.web.util.ResponseUtil;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/store")
public class StoreResource {
    private static final String ENTITY_NAME = "store";
    private final Logger log = LoggerFactory.getLogger(StoreResource.class);
    private final StoreService storeService;

    public StoreResource(StoreService storeService) {
        this.storeService = storeService;
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @PostMapping("/create")
    public ResponseEntity<StoreEntity> create(@Valid @RequestPart("entity") StoreEntity entity,
                                              @RequestPart(value = "image", required = false) MultipartFile image) {
        if (entity.getId() != null || entity.getGuid() != null) throw new BadRequestException("error.create.idExists");
        log.debug("REST request to create {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(storeService.create(entity, image));
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @PutMapping("/update")
    public ResponseEntity<StoreEntity> update(@Valid @RequestPart StoreEntity entity,
                                              @RequestPart(value = "image", required = false) MultipartFile image) {
        if (entity.getGuid() == null) throw new BadRequestException("error.update.idNull");
        log.debug("REST request to update {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(storeService.update(entity, image));
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @GetMapping("/list")
    public ResponseEntity<Page<StoreEntity>> getList(@RequestParam(value = "search", defaultValue = "") String search,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "20") int size,
                                                     @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                     @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        log.debug("REST request to get list {} with page request: {}", ENTITY_NAME, request);
        return ResponseEntity.ok(storeService.getList(search, request));
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @GetMapping("/get/{storeGuid}")
    public ResponseEntity<StoreEntity> getOne(@PathVariable String storeGuid) {
        log.debug("REST request to get {} with guid : {}", ENTITY_NAME, storeGuid);
        Optional<StoreEntity> store = storeService.getOneByGuid(storeGuid);
        return ResponseUtil.wrapOrNotFound(store);
    }

    @Secured(AuthoritiesConstants.ADMIN)
    @DeleteMapping("/delete/{storeGuid}")
    public ResponseEntity<Void> delete(@PathVariable String storeGuid) {
        log.debug("REST request to delete {} with guid : {}", ENTITY_NAME, storeGuid);
        storeService.delete(storeGuid);
        return ResponseEntity.noContent().build();
    }
}
