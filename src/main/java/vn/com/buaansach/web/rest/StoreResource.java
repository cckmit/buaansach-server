package vn.com.buaansach.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.StoreEntity;
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

    @PostMapping("/create")
    public ResponseEntity<StoreEntity> create(@Valid @RequestPart("entity") StoreEntity entity,
                                              @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request to save {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(storeService.create(entity, image));
    }

    @PutMapping("/update")
    public ResponseEntity<StoreEntity> update(@Valid @RequestPart StoreEntity entity,
                                              @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request to update {} : {}", ENTITY_NAME, entity);
        return ResponseEntity.ok(storeService.update(entity, image));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<StoreEntity>> getList(@RequestParam(value = "search", defaultValue = "") String search,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "20") int size,
                                                     @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                     @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        log.debug("REST request to get list {}", ENTITY_NAME);
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        return ResponseEntity.ok(storeService.getList(search, request));
    }

    @GetMapping("/get/{storeCode}")
    public ResponseEntity<StoreEntity> getOne(@PathVariable String storeCode) {
        log.debug("REST request to get {} : {}", ENTITY_NAME, storeCode);
        Optional<StoreEntity> store = storeService.getOne(storeCode);
        return ResponseUtil.wrapOrNotFound(store);
    }

    @DeleteMapping("/delete/{storeCode}")
    public ResponseEntity<Void> delete(@PathVariable String storeCode) {
        log.debug("REST request to delete {} : {}", ENTITY_NAME, storeCode);
        storeService.delete(storeCode);
        return ResponseEntity.noContent().build();
    }
}
