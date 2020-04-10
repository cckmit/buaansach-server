package vn.com.buaansach.service.guest;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.repository.ProductRepository;

import java.util.List;

@Service
public class GuestProductService {
    private final ProductRepository productRepository;

    public GuestProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> getList() {
        return productRepository.findAll();
    }
}
