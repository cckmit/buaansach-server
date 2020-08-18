package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosOrderFeedbackService;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderFeedbackDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/pos/order-feedback")
@RequiredArgsConstructor
public class PosOrderFeedbackResource {
    private final String ENTITY_NAME = "pos-order-feedback";
    private final Logger log = LoggerFactory.getLogger(PosOrderFeedbackResource.class);
    private final PosOrderFeedbackService posOrderFeedbackService;

    @PostMapping("/send-feedback")
    public ResponseEntity<Void> sendFeedback(@Valid @RequestBody PosOrderFeedbackDTO payload) {
        log.debug("REST request from user [{}] to send [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        posOrderFeedbackService.sendFeedback(payload);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-feedback/{orderGuid}")
    public ResponseEntity<PosOrderFeedbackDTO> getFeedback(@PathVariable String orderGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        return ResponseEntity.ok(posOrderFeedbackService.getFeedback(orderGuid));
    }
}
