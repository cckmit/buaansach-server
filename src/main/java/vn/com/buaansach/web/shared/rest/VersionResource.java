package vn.com.buaansach.web.shared.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.entity.brand.VersionEntity;
import vn.com.buaansach.web.shared.service.VersionService;

@RestController
@RequestMapping("/api/v1/public/version")
@RequiredArgsConstructor
public class VersionResource {
    private final VersionService versionService;

    @GetMapping("/latest/{versionType}")
    public ResponseEntity<String> getLatestVersion(@PathVariable String versionType) {
        return ResponseEntity.ok(versionService.getLatestVersion(versionType));
    }
}
