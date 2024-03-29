package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.customer.CustomerPointLogEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.PointLogReason;
import vn.com.buaansach.entity.enumeration.PointLogType;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.shared.service.dto.readwrite.UsePointDTO;
import vn.com.buaansach.web.shared.repository.customer.CustomerPointLogRepository;
import vn.com.buaansach.web.shared.repository.customer.CustomerRepository;
import vn.com.buaansach.web.shared.repository.order.OrderRepository;
import vn.com.buaansach.web.shared.repository.store.SeatRepository;
import vn.com.buaansach.web.shared.repository.store.StoreRepository;
import vn.com.buaansach.web.shared.repository.user.UserRepository;
import vn.com.buaansach.web.shared.websocket.WebsocketService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerPointLogRepository customerPointLogRepository;
    private final UserRepository userRepository;
    private final PriceService priceService;
    private final OrderRepository orderRepository;
    private final SeatRepository seatRepository;
    private final StoreRepository storeRepository;
    private final WebsocketService websocketService;

    @Transactional
    public void createdCustomer(UUID userGuid) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUserGuid(userGuid);
        customerEntity.setCustomerPoint(Constants.INITIAL_CUSTOMER_POINT);
        customerRepository.save(customerEntity);

        CustomerPointLogEntity pointLogEntity = new CustomerPointLogEntity();
        pointLogEntity.setOrderGuid(null);
        pointLogEntity.setPointLogReason(PointLogReason.INITIAL_ACCOUNT.name());
        pointLogEntity.setUserGuid(userGuid);
        pointLogEntity.setPointLogValue(Constants.INITIAL_CUSTOMER_POINT);
        pointLogEntity.setPointLogType(PointLogType.ADD);
        customerPointLogRepository.save(pointLogEntity);
    }

    @Transactional
    public void earnPoint(OrderEntity orderEntity) {
        if (orderEntity.getUserGuid() == null) return;
        CustomerEntity customerEntity = customerRepository.findOneByUserGuid(orderEntity.getUserGuid())
                .orElse(null);
        if (customerEntity == null) return;
        int payAmount = priceService.calculatePayAmount(orderEntity);
        int earnedPoint = payAmount / 100 * 5;

        if (earnedPoint == 0) return;

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + earnedPoint);

        CustomerPointLogEntity pointLogEntity = new CustomerPointLogEntity();
        pointLogEntity.setOrderGuid(orderEntity.getGuid());
        pointLogEntity.setPointLogReason(PointLogReason.EARN_FROM_ORDER.name());
        pointLogEntity.setUserGuid(customerEntity.getUserGuid());
        pointLogEntity.setPointLogValue(earnedPoint);
        pointLogEntity.setPointLogType(PointLogType.ADD);

        customerRepository.save(customerEntity);
        customerPointLogRepository.save(pointLogEntity);
    }

    @Transactional
    public void usePoint(OrderEntity orderEntity, int orderPointValue) {
        if (orderPointValue < 0) throw new BadRequestException(ErrorCode.POINT_USAGE_MUST_GREATER_THAN_EQUAL_ZERO);

        if (orderEntity.getOrderCustomerPhone() == null)
            throw new BadRequestException(ErrorCode.ORDER_CUSTOMER_PHONE_EMPTY);

        UserEntity userEntity = userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!userEntity.getUserPhone().equals(orderEntity.getOrderCustomerPhone()))
            throw new BadRequestException(ErrorCode.USER_PHONE_NOT_MATCH_ORDER_PHONE);

        CustomerEntity customerEntity = customerRepository.findOneByUserPhone(orderEntity.getOrderCustomerPhone())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + orderEntity.getOrderPointValue());

        if (customerEntity.getCustomerPoint() < orderPointValue)
            throw new BadRequestException(ErrorCode.CUSTOMER_POINT_NOT_ENOUGH);

        orderEntity.setOrderPointValue(orderPointValue);

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() - orderPointValue);

        orderRepository.save(orderEntity);
        customerRepository.save(customerEntity);

        StoreEntity storeEntity = storeRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(()-> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        websocketService.sendUsePointNotification(storeEntity.getGuid(), orderEntity);
    }

    public void userPoint(UsePointDTO payload) {
        OrderEntity orderEntity = orderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        SeatEntity seatEntity = seatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        if (seatEntity.isSeatLocked())
            throw new BadRequestException(ErrorCode.SEAT_LOCKED);
        if (orderEntity.getOrderStatus().equals(OrderStatus.PURCHASED))
            throw new BadRequestException(ErrorCode.ORDER_PURCHASED);
        if (orderEntity.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new BadRequestException(ErrorCode.ORDER_CANCELLED);
        usePoint(orderEntity, payload.getOrderPointValue());
    }

    @Transactional
    public void rollbackPoint(OrderEntity orderEntity) {
        if (orderEntity.getUserGuid() == null) return;
        if (orderEntity.getOrderPointValue() == 0) return;

        CustomerEntity customerEntity = customerRepository.findOneByUserGuid(orderEntity.getUserGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + orderEntity.getOrderPointValue());
        orderEntity.setOrderPointValue(0);
        orderRepository.save(orderEntity);
        customerRepository.save(customerEntity);
    }

    public void addUsePointLog(OrderEntity orderEntity) {
        if (orderEntity.getUserGuid() == null) return;
        if (orderEntity.getOrderPointValue() == 0) return;

        CustomerPointLogEntity pointLogEntity = new CustomerPointLogEntity();
        pointLogEntity.setOrderGuid(orderEntity.getGuid());
        pointLogEntity.setPointLogReason(PointLogReason.USE_FOR_ORDER.name());
        pointLogEntity.setUserGuid(orderEntity.getUserGuid());
        pointLogEntity.setPointLogValue(orderEntity.getOrderPointValue());
        pointLogEntity.setPointLogType(PointLogType.SUBTRACT);

        customerPointLogRepository.save(pointLogEntity);
    }

    public List<CustomerPointLogEntity> getListPointLog() {
        UserEntity currentUser = userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return customerPointLogRepository.findTop20ByUserGuidOrderByIdDesc(currentUser.getGuid());
    }

    public CustomerEntity getCustomerInfo() {
        UserEntity currentUser = userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return customerRepository.findOneByUserGuid(currentUser.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
    }
}
