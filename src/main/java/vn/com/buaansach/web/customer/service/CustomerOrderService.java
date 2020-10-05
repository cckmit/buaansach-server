package vn.com.buaansach.web.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.OrderTimelineStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.web.customer.repository.customer.CustomerCustomerRepository;
import vn.com.buaansach.web.customer.repository.order.CustomerOrderRepository;
import vn.com.buaansach.web.customer.repository.store.CustomerStoreRepository;
import vn.com.buaansach.web.customer.websocket.CustomerSocketService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerCustomerRepository customerCustomerRepository;
    private final CustomerStoreRepository customerStoreRepository;
    private final CustomerSocketService customerSocketService;

    @Transactional
    public void updateOrderPhone(OrderEntity entity, String currentUser) {
        OrderEntity updateOrder = customerOrderRepository.findOneByGuid(entity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        if (entity.getOrderCustomerPhone() == null && updateOrder.getOrderCustomerPhone() == null) return;
        if (entity.getOrderCustomerPhone() != null && entity.getOrderCustomerPhone().equals(updateOrder.getOrderCustomerPhone()))
            return;

        /* rollback point for previous phone */
        if (updateOrder.getOrderPointValue() != 0) {
            CustomerEntity customerEntity = customerCustomerRepository.findOneByUserPhone(updateOrder.getOrderCustomerPhone())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
            customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + updateOrder.getOrderPointValue());
            updateOrder.setOrderPointValue(0);
            updateOrder.setOrderPointCost(0);
            customerCustomerRepository.save(customerEntity);
        }

        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(updateOrder.getOrderStatusTimeline(),
                OrderTimelineStatus.UPDATE_PHONE,
                currentUser,
                entity.getOrderCustomerPhone());
        updateOrder.setOrderStatusTimeline(newTimeline);
        updateOrder.setOrderCustomerPhone(entity.getOrderCustomerPhone());
        customerOrderRepository.save(updateOrder);

        StoreEntity storeEntity = customerStoreRepository.findOneBySeatGuid(updateOrder.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        customerSocketService.sendUpdatePhoneNotification(storeEntity.getGuid(), updateOrder);
    }
}
