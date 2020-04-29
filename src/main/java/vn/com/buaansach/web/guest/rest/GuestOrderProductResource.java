package vn.com.buaansach.web.guest.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/guest/order-product")
public class GuestOrderProductResource {
    private final String ENTITY_NAME = "guest-order-product";
    private final Logger log = LoggerFactory.getLogger(GuestOrderProductResource.class);
}
