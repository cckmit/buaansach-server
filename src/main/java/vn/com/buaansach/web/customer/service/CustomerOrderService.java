package vn.com.buaansach.web.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.OrderTimelineStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.web.customer.repository.customer.CustomerCustomerRepository;
import vn.com.buaansach.web.customer.repository.order.CustomerOrderRepository;
import vn.com.buaansach.web.customer.repository.store.CustomerStoreRepository;
import vn.com.buaansach.web.customer.repository.user.CustomerUserRepository;
import vn.com.buaansach.web.customer.websocket.CustomerSocketService;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerCustomerRepository customerCustomerRepository;
    private final CustomerStoreRepository customerStoreRepository;
    private final CustomerSocketService customerSocketService;
    private final CustomerUserRepository customerUserRepository;

    @Transactional
    public void updateOrderUser(UUID orderGuid) {
        OrderEntity updateOrder = customerOrderRepository.findOneByGuid(orderGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        UserEntity userEntity = customerUserRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (updateOrder.getUserGuid() == userEntity.getGuid()) return;

        /* rollback point for previous user */
        if (updateOrder.getOrderPointValue() != 0) {
            CustomerEntity customerEntity = customerCustomerRepository.findOneByUserGuid(updateOrder.getUserGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
            customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + updateOrder.getOrderPointValue());
            updateOrder.setOrderPointValue(0);
            customerCustomerRepository.save(customerEntity);
        }

        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(updateOrder.getOrderStatusTimeline(),
                OrderTimelineStatus.UPDATE_PHONE,
                userEntity.getUserLogin(),
                userEntity.getUserPhone());
        updateOrder.setOrderStatusTimeline(newTimeline);
        updateOrder.setOrderCustomerPhone(userEntity.getUserPhone());
        updateOrder.setUserGuid(userEntity.getGuid());
        customerOrderRepository.save(updateOrder);

        StoreEntity storeEntity = customerStoreRepository.findOneBySeatGuid(updateOrder.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        customerSocketService.sendUpdatePhoneNotification(storeEntity.getGuid(), updateOrder);
    }
}
