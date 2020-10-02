package vn.com.buaansach.web.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.OrderTimelineStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.web.customer.repository.customer.CustomerCustomerRepository;
import vn.com.buaansach.web.customer.repository.order.CustomerOrderRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerCustomerRepository customerCustomerRepository;

    @Transactional
    public void updateOrderPhone(OrderEntity entity, String currentUser) {
        OrderEntity update = customerOrderRepository.findOneByGuid(entity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        if (entity.getOrderCustomerPhone() == null && update.getOrderCustomerPhone() == null) return;
        if (entity.getOrderCustomerPhone() != null && entity.getOrderCustomerPhone().equals(update.getOrderCustomerPhone()))
            return;

        /* rollback point for previous phone */
        if (update.getOrderPointValue() != 0) {
            CustomerEntity customerEntity = customerCustomerRepository.findOneByUserPhone(update.getOrderCustomerPhone())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
            customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + update.getOrderPointValue());
            update.setOrderPointValue(0);
            update.setOrderPointCost(0);
            customerCustomerRepository.save(customerEntity);
        }

        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(update.getOrderStatusTimeline(),
                OrderTimelineStatus.UPDATE_PHONE,
                currentUser,
                entity.getOrderCustomerPhone());
        update.setOrderStatusTimeline(newTimeline);
        update.setOrderCustomerPhone(entity.getOrderCustomerPhone());
        customerOrderRepository.save(update);
    }
}
