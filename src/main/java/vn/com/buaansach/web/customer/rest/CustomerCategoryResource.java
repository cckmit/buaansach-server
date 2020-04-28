package vn.com.buaansach.web.customer.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.web.pos.rest.PosAreaResource;

@RestController
@RequestMapping("/api/v1/customer/category")
public class CustomerCategoryResource {
    private final String ENTITY_NAME = "customer-category";
    private final Logger log = LoggerFactory.getLogger(CustomerCategoryResource.class);
}
