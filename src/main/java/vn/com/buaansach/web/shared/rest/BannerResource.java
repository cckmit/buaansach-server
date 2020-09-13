package vn.com.buaansach.web.shared.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.entity.brand.BannerEntity;
import vn.com.buaansach.web.shared.service.BannerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/banner")
@RequiredArgsConstructor
public class BannerResource {
    private final BannerService bannerService;

    @GetMapping("/list")
    public ResponseEntity<List<BannerEntity>> getListBanner() {
        return ResponseEntity.ok(bannerService.getListBanner());
    }
}
