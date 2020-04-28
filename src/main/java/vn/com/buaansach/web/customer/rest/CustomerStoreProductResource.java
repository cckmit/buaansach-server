package vn.com.buaansach.web.customer.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.web.customer.service.CustomerStoreProductService;
import vn.com.buaansach.web.customer.service.dto.CustomerStoreProductDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer/store-product")
public class CustomerStoreProductResource {
    private final String ENTITY_NAME = "customer-store-product";
    private final Logger log = LoggerFactory.getLogger(CustomerStoreProductResource.class);
    private final CustomerStoreProductService customerStoreProductService;

    public CustomerStoreProductResource(CustomerStoreProductService customerStoreProductService) {
        this.customerStoreProductService = customerStoreProductService;
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<CustomerStoreProductDTO>> getListStoreProduct(@PathVariable String storeGuid) {
        return ResponseEntity.ok(customerStoreProductService.getListStoreProduct(storeGuid));
    }
}
