package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;

import java.util.UUID;

@Service
public class PosCustomerService {
    public void createCustomerIfNotExist(String customerPhone){

    }

    public void createCustomer(String customerPhone){
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setGuid(UUID.randomUUID());
        customerEntity.setCustomerPhone(customerPhone);
    }
}
