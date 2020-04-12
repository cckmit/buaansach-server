package vn.com.buaansach.web.customer.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.web.customer.service.dto.CustomerProductDTO;
import vn.com.buaansach.web.customer.service.CustomerProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/guest/product")
public class CustomerProductResource {
    private final CustomerProductService customerProductService;

    public CustomerProductResource(CustomerProductService customerProductService) {
        this.customerProductService = customerProductService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CustomerProductDTO>> getListProduct() {
        return ResponseEntity.ok(customerProductService.getList().stream()
                .map(CustomerProductDTO::new)
                .collect(Collectors.toList()));
    }
}
