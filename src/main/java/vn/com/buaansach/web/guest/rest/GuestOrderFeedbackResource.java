package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestOrderFeedbackService;
import vn.com.buaansach.web.guest.service.dto.write.GuestOrderFeedbackDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/guest/order-feedback")
@RequiredArgsConstructor
public class GuestOrderFeedbackResource {
    private final String ENTITY_NAME = "guest-order-feedback";
    private final Logger log = LoggerFactory.getLogger(GuestOrderFeedbackResource.class);
    private final GuestOrderFeedbackService guestOrderFeedbackService;

    @PostMapping("/send-feedback")
    public ResponseEntity<Void> sendFeedback(@Valid @RequestBody GuestOrderFeedbackDTO payload) {
        log.debug("REST request from user [{}] to send [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        guestOrderFeedbackService.sendFeedback(payload);
        return ResponseEntity.ok().build();
    }
}
