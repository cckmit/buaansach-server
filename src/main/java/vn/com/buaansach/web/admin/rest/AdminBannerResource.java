package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.brand.BannerEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminBannerService;

import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/banner")
@RequiredArgsConstructor
public class AdminBannerResource {
    private static final String ENTITY_NAME = "admin-banner";
    private final Logger log = LoggerFactory.getLogger(AdminBannerResource.class);
    private final AdminBannerService adminBannerService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BannerEntity> createBanner(@RequestPart("payload") BannerEntity payload, @RequestPart("image") MultipartFile image) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminBannerService.createBanner(payload, image));
    }

    @PutMapping("/update")
    public ResponseEntity<BannerEntity> updateBanner(@RequestPart("payload") BannerEntity payload, @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminBannerService.updateBanner(payload, image));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BannerEntity>> getListBanner() {
        log.debug("REST request from user [{}] to list [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminBannerService.getListBanner());
    }

    @DeleteMapping("/delete/{bannerGuid}")
    public ResponseEntity<Void> deleteBanner(@PathVariable String bannerGuid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, bannerGuid);
        adminBannerService.deleteBanner(bannerGuid);
        return ResponseEntity.noContent().build();
    }
}
