package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestOrderProductService;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guest/order-product")
@RequiredArgsConstructor
public class GuestOrderProductResource {
    private final String ENTITY_NAME = "guest-order-product";
    private final Logger log = LoggerFactory.getLogger(GuestOrderProductResource.class);
    private final GuestOrderProductService guestOrderProductService;

    @GetMapping("/list-by-order/{orderGuid}")
    public ResponseEntity<List<GuestOrderProductDTO>> getOrderProduct(@PathVariable String orderGuid) {
        log.debug("REST request from user [{}] to get [{}] by order : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, orderGuid);
        return ResponseEntity.ok(guestOrderProductService.getOrderProduct(orderGuid));
    }
}
