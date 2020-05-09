package vn.com.buaansach.web.guest.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestCategoryService;
import vn.com.buaansach.web.guest.service.dto.read.GuestCategoryDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/guest/category")
public class GuestCategoryResource {
    private final String ENTITY_NAME = "guest-category";
    private final Logger log = LoggerFactory.getLogger(GuestCategoryResource.class);
    private final GuestCategoryService guestCategoryService;

    public GuestCategoryResource(GuestCategoryService guestCategoryService) {
        this.guestCategoryService = guestCategoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<GuestCategoryDTO>> getListPosCategoryDTO() {
        log.debug("REST request from user [{}] to list {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(guestCategoryService.getListGuestCategoryDTO().stream()
                .map(GuestCategoryDTO::new)
                .collect(Collectors.toList()));
    }
}
