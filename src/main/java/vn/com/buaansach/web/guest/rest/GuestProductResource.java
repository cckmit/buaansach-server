package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestProductService;
import vn.com.buaansach.web.guest.service.dto.read.GuestProductDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guest/product")
@RequiredArgsConstructor
public class GuestProductResource {
    private final String ENTITY_NAME = "guest-product";
    private final Logger log = LoggerFactory.getLogger(GuestProductResource.class);
    private final GuestProductService guestProductService;

    @GetMapping("/list")
    public ResponseEntity<List<GuestProductDTO>> getAllProduct() {
        log.debug("REST request from user [{}] to list [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(guestProductService.getAllProduct());
    }
}
