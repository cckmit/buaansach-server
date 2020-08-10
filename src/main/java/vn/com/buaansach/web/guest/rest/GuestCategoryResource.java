package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
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
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreCategoryDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guest/category")
@RequiredArgsConstructor
public class GuestCategoryResource {
    private final String ENTITY_NAME = "guest-category";
    private final Logger log = LoggerFactory.getLogger(GuestCategoryResource.class);
    private final GuestCategoryService guestCategoryService;

    @GetMapping("/list-by-seat/{seatGuid}")
    public ResponseEntity<List<GuestStoreCategoryDTO>> getListGuestStoreCategoryDTO(@PathVariable String seatGuid) {
        log.debug("REST request from user [{}] to list [{}] by seat: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, seatGuid);
        return ResponseEntity.ok(guestCategoryService.getListGuestStoreCategoryDTO(seatGuid));
    }

    @GetMapping("/list")
    public ResponseEntity<List<GuestCategoryDTO>> getListCategoryDTO() {
        log.debug("REST request from user [{}] to list [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(guestCategoryService.getListCategoryDTO());
    }
}
