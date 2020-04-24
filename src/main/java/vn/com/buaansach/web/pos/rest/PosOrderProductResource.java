package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pos/order-product")
public class PosOrderProductResource {
    private final String ENTITY_NAME = "pos-order-product";
    private final Logger log = LoggerFactory.getLogger(PosOrderProductResource.class);
}
