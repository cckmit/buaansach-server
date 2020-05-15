package vn.com.buaansach.web.manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.manager.repository.ManagerCustomerRepository;
import vn.com.buaansach.web.manager.service.dto.ManagerUpdateCustomerDTO;

@Service
@RequiredArgsConstructor
public class ManagerCustomerService {
    private final ManagerCustomerRepository managerCustomerRepository;

    public void updateCustomer(ManagerUpdateCustomerDTO payload) {
        CustomerEntity customerEntity = managerCustomerRepository.findOneByCustomerPhone(payload.getCustomerPhone())
                .orElseThrow(() -> new ResourceNotFoundException("manager@customerNotFound@" + payload.getCustomerPhone()));
        customerEntity.setCustomerZaloStatus(payload.getCustomerZaloStatus());
        managerCustomerRepository.save(customerEntity);
    }
}
