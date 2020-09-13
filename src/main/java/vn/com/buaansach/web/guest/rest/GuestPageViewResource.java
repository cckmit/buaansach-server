package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.entity.statistic.PageViewEntity;
import vn.com.buaansach.web.guest.service.GuestPageViewService;

@RestController
@RequestMapping("/api/v1/guest/page-view")
@RequiredArgsConstructor
public class GuestPageViewResource {
    private final GuestPageViewService guestPageViewService;

    @PostMapping("/add")
    public ResponseEntity<Void> addPageView(@RequestBody PageViewEntity payload) {
        guestPageViewService.addPageView(payload);
        return ResponseEntity.noContent().build();
    }
}
