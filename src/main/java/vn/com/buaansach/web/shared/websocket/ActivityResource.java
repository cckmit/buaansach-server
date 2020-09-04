package vn.com.buaansach.web.shared.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.shared.websocket.dto.ActivityDTO;
import vn.com.buaansach.web.shared.rest.UserResource;

import java.util.Set;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/tracker")
public class ActivityResource {
    private final String ENTITY_NAME = "tracker";
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @GetMapping("/active-users")
    public ResponseEntity<Set<ActivityDTO>> getActiveUsers() {
        log.debug("REST request from user [{}] to get active users", SecurityUtils.getCurrentUserLogin());
        return ResponseEntity.ok(ActivityController.activeUsers);
    }
}
