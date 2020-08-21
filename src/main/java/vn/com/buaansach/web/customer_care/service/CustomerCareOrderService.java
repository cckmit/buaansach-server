package vn.com.buaansach.web.customer_care.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.customer_care.repository.CustomerCareOrderProductRepository;
import vn.com.buaansach.web.customer_care.repository.CustomerCareOrderRepository;
import vn.com.buaansach.web.customer_care.repository.CustomerCareVoucherCodeRepository;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareOrderDTO;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareVoucherCodeDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerCareOrderService {
    private final CustomerCareOrderRepository customerCareOrderRepository;
    private final CustomerCareOrderProductRepository customerCareOrderProductRepository;
    private final CustomerCareVoucherCodeRepository customerCareVoucherCodeRepository;

    public CustomerCareOrderDTO getOrderInfo(String orderGuid) {
        OrderEntity orderEntity = customerCareOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new ResourceNotFoundException("customerCare@orderNotFound@" + orderGuid));
        CustomerCareOrderDTO result = new CustomerCareOrderDTO(orderEntity);

        result.setListOrderProduct(customerCareOrderProductRepository.findListOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }
}
