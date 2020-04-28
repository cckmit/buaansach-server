package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.web.customer.repository.CustomerStoreProductRepository;
import vn.com.buaansach.web.customer.service.dto.CustomerStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerStoreProductService {
    private final CustomerStoreProductRepository customerStoreProductRepository;

    public CustomerStoreProductService(CustomerStoreProductRepository customerStoreProductRepository) {
        this.customerStoreProductRepository = customerStoreProductRepository;
    }

    public List<CustomerStoreProductDTO> getListStoreProduct(String storeGuid) {
        return customerStoreProductRepository.findListCustomerStoreProductDTO(UUID.fromString(storeGuid));
    }
}
