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
import vn.com.buaansach.entity.brand.VersionEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminBannerService;
import vn.com.buaansach.web.admin.service.AdminVersionService;

import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/version")
@RequiredArgsConstructor
public class AdminVersionResource {
    private static final String ENTITY_NAME = "admin-version";
    private final Logger log = LoggerFactory.getLogger(AdminVersionResource.class);
    private final AdminVersionService adminVersionService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VersionEntity> createVersion(@RequestBody VersionEntity payload) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminVersionService.createVersion(payload));
    }

    @GetMapping("/list")
    public ResponseEntity<List<VersionEntity>> getAllVersion() {
        log.debug("REST request from user [{}] to list [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminVersionService.getAllVersion());
    }

    @GetMapping("/latest/{versionType}")
    public ResponseEntity<VersionEntity> getLatestVersion(@PathVariable String versionType) {
        log.debug("REST request from user [{}] to get latest [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(adminVersionService.getLatestVersion(versionType));
    }

    @DeleteMapping("/delete/{versionGuid}")
    public ResponseEntity<Void> deleteVersion(@PathVariable String versionGuid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, versionGuid);
        adminVersionService.deleteVersion(versionGuid);
        return ResponseEntity.noContent().build();
    }
}
