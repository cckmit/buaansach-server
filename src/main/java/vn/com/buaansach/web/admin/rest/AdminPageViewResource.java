package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.entity.statistic.PageViewEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminPageViewService;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminPageViewDTO;
import vn.com.buaansach.web.guest.service.GuestPageViewService;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/page-view")
@RequiredArgsConstructor
public class AdminPageViewResource {
    private static final String ENTITY_NAME = "admin-page-view";
    private final Logger log = LoggerFactory.getLogger(AdminPageViewResource.class);
    private final AdminPageViewService adminPageViewService;

    @PostMapping("/statistic")
    public ResponseEntity<AdminPageViewDTO> getPageViewStatistic(@Valid @RequestBody AdminPageViewDTO payload) {
        log.debug("REST request from user [{}] to statistic [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminPageViewService.getPageViewStatistic(payload));
    }
}
