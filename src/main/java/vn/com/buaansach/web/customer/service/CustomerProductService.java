package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.repository.ProductRepository;

import java.util.List;

@Service
public class CustomerProductService {
    private final ProductRepository productRepository;

    public CustomerProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> getList() {
        return productRepository.findAll();
    }
}
