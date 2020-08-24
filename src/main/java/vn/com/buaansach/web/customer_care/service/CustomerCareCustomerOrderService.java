package vn.com.buaansach.web.customer_care.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerOrderEntity;
import vn.com.buaansach.entity.enumeration.CustomerCareStatus;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.customer_care.repository.CustomerCareCustomerOrderRepository;
import vn.com.buaansach.web.customer_care.service.dto.readwrite.CustomerCareCustomerOrderDTO;
import vn.com.buaansach.web.customer_care.service.dto.write.CustomerCareCustomerOrderParamsDTO;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerCareCustomerOrderService {
    private final CustomerCareCustomerOrderRepository customerCareCustomerOrderRepository;

    public List<CustomerCareCustomerOrderDTO> getListCustomerOrder(CustomerCareCustomerOrderParamsDTO payload) {
        if (payload.getCustomerPhone() == null) payload.setCustomerPhone("");
        String customerPhone = payload.getCustomerPhone() == null ? "" : payload.getCustomerPhone();
        Instant startDate = payload.getStartDate();
        Instant endDate = payload.getEndDate();
        CustomerCareStatus customerCareStatus = payload.getCustomerCareStatus();
        int minCount = payload.getOrderCountMin() != null ? payload.getOrderCountMin() : 0;
        Integer maxCount = payload.getOrderCountMax();
        List<CustomerCareCustomerOrderDTO> result;

        if (customerCareStatus != null){
            if (maxCount != null){
                result = customerCareCustomerOrderRepository.findListCustomerOrder(customerPhone, startDate, endDate, customerCareStatus, minCount, maxCount);
            } else {
                result = customerCareCustomerOrderRepository.findListCustomerOrder(customerPhone, startDate, endDate, customerCareStatus, minCount);
            }
        } else {
            if (maxCount != null){
                result = customerCareCustomerOrderRepository.findListCustomerOrder(customerPhone, startDate, endDate, minCount, maxCount);
            } else {
                result = customerCareCustomerOrderRepository.findListCustomerOrder(customerPhone, startDate, endDate, minCount);
            }
        }
        return result;
    }

    public void updateCustomerOrder(CustomerCareCustomerOrderDTO payload) {
        CustomerOrderEntity customerOrderEntity = customerCareCustomerOrderRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(()-> new NotFoundException("customerCare@customerOrderNotFound@" + payload.getGuid()));
        customerOrderEntity.setCustomerCareStatus(payload.getCustomerCareStatus());
        customerOrderEntity.setCustomerOrderFeedback(payload.getCustomerOrderFeedback());
        customerCareCustomerOrderRepository.save(customerOrderEntity);
    }
}
