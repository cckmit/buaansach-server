package vn.com.buaansach.web.guest.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestCategoryService;
import vn.com.buaansach.web.guest.service.dto.read.GuestCategoryDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guest/category")
public class GuestCategoryResource {
    private final String ENTITY_NAME = "guest-category";
    private final Logger log = LoggerFactory.getLogger(GuestCategoryResource.class);
    private final GuestCategoryService guestCategoryService;

    public GuestCategoryResource(GuestCategoryService guestCategoryService) {
        this.guestCategoryService = guestCategoryService;
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<GuestCategoryDTO>> getListPosCategoryDTO(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list {} by store: {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(guestCategoryService.getListGuestCategoryDTO(storeGuid));
    }
}
