package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.web.customer.repository.CustomerProductRepository;

import java.util.List;

@Service
public class CustomerProductService {
    private final CustomerProductRepository customerProductRepository;

    public CustomerProductService(CustomerProductRepository customerProductRepository) {
        this.customerProductRepository = customerProductRepository;
    }

    public List<ProductEntity> getList() {
        return customerProductRepository.findAll();
    }
}
