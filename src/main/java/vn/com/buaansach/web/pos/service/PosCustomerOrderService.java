package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerOrderEntity;
import vn.com.buaansach.entity.enumeration.CustomerCareStatus;
import vn.com.buaansach.web.pos.repository.PosCustomerOrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosCustomerOrderService {
    private final PosCustomerOrderRepository posCustomerOrderRepository;

    public void addCustomerOrder(String customerPhone, UUID orderGuid, UUID storeGuid) {
        CustomerOrderEntity customerOrderEntity = new CustomerOrderEntity();
        customerOrderEntity.setGuid(UUID.randomUUID());
        customerOrderEntity.setCustomerPhone(customerPhone);
        customerOrderEntity.setCustomerCareStatus(CustomerCareStatus.PENDING);

        List<CustomerOrderEntity> list = posCustomerOrderRepository.findByCustomerPhone(customerPhone);
        customerOrderEntity.setOrderGuid(orderGuid);
        customerOrderEntity.setOrderCount(list.size() + 1);
        customerOrderEntity.setStoreGuid(storeGuid);

        posCustomerOrderRepository.save(customerOrderEntity);
    }
}
