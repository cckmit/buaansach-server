package vn.com.buaansach.web.customer_care.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.customer_care.repository.CustomerCareCustomerRepository;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareCustomerDTO;
import vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareStatisticDTO;
import vn.com.buaansach.web.customer_care.service.dto.write.CustomerCareUpdateCustomerDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerCareCustomerService {
    private final CustomerCareCustomerRepository customerCareCustomerRepository;

    public void updateCustomer(CustomerCareUpdateCustomerDTO payload) {
        CustomerEntity customerEntity = customerCareCustomerRepository.findOneByCustomerPhone(payload.getCustomerPhone())
                .orElseThrow(() -> new NotFoundException("customerCare@customerNotFound@" + payload.getCustomerPhone()));
        customerEntity.setCustomerZaloStatus(payload.getCustomerZaloStatus());
        customerCareCustomerRepository.save(customerEntity);
    }

    public CustomerCareStatisticDTO getStatistic(CustomerCareStatisticDTO payload) {
        List<CustomerEntity> list = customerCareCustomerRepository.findByDateRange(payload.getStartDate(), payload.getEndDate());
        payload.setListCustomer(list.stream().map(CustomerCareCustomerDTO::new).collect(Collectors.toList()));
        return payload;
    }
}
