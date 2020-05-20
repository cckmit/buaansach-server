package vn.com.buaansach.web.customer_care.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.customer_care.repository.CustomerCareCustomerRepository;
import vn.com.buaansach.web.customer_care.service.dto.CustomerCareUpdateCustomerDTO;

@Service
@RequiredArgsConstructor
public class CustomerCareCustomerService {
    private final CustomerCareCustomerRepository customerCareCustomerRepository;

    public void updateCustomer(CustomerCareUpdateCustomerDTO payload) {
        CustomerEntity customerEntity = customerCareCustomerRepository.findOneByCustomerPhone(payload.getCustomerPhone())
                .orElseThrow(() -> new ResourceNotFoundException("customerCare@customerNotFound@" + payload.getCustomerPhone()));
        customerEntity.setCustomerZaloStatus(payload.getCustomerZaloStatus());
        customerCareCustomerRepository.save(customerEntity);
    }
}
