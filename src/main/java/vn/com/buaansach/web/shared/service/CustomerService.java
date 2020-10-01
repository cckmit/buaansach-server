package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.customer.CustomerPointLogEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.PointLogType;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.customer.service.write.CustomerUsePointDTO;
import vn.com.buaansach.web.shared.repository.customer.CustomerPointLogRepository;
import vn.com.buaansach.web.shared.repository.customer.CustomerRepository;
import vn.com.buaansach.web.shared.repository.order.OrderRepository;
import vn.com.buaansach.web.shared.repository.store.SeatRepository;
import vn.com.buaansach.web.shared.repository.user.UserRepository;

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

    public void createdCustomer(UUID userGuid) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUserGuid(userGuid);
        customerEntity.setCustomerPoint(0);
        customerRepository.save(customerEntity);
    }

    @Transactional
    public void earnPoint(OrderEntity orderEntity) {
        if (orderEntity.getOrderCustomerPhone() == null) return;
        CustomerEntity customerEntity = customerRepository.findOneByUserPhone(orderEntity.getOrderCustomerPhone())
                .orElse(null);
        if (customerEntity == null) return;
        int payAmount = priceService.calculatePayAmount(orderEntity);
        int div = payAmount / 1000;
        int remain = payAmount % 1000;
        int earnedPoint = remain == 0 ? div : div + 1;

        if (earnedPoint == 0) return;

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + earnedPoint);

        CustomerPointLogEntity pointLogEntity = new CustomerPointLogEntity();
        pointLogEntity.setOrderGuid(orderEntity.getGuid());
        pointLogEntity.setUserGuid(customerEntity.getUserGuid());
        pointLogEntity.setPointLogValue(earnedPoint);
        pointLogEntity.setPointLogType(PointLogType.ADD);

        customerRepository.save(customerEntity);
        customerPointLogRepository.save(pointLogEntity);
    }

    @Transactional
    public void assignPoint(OrderEntity orderEntity, int point) {
        if (point < 0) throw new BadRequestException(ErrorCode.POINT_USAGE_MUST_GREATER_THAN_EQUAL_ZERO);

        if (orderEntity.getOrderCustomerPhone() == null)
            throw new BadRequestException(ErrorCode.ORDER_CUSTOMER_PHONE_EMPTY);

        UserEntity userEntity = userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!userEntity.getUserPhone().equals(orderEntity.getOrderCustomerPhone()))
            throw new BadRequestException(ErrorCode.USER_PHONE_NOT_MATCH_ORDER_PHONE);

        CustomerEntity customerEntity = customerRepository.findOneByUserPhone(orderEntity.getOrderCustomerPhone())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + orderEntity.getOrderPointValue());

        if (customerEntity.getCustomerPoint() < point)
            throw new BadRequestException(ErrorCode.CUSTOMER_POINT_NOT_ENOUGH);

        orderEntity.setOrderPointValue(point);
        orderEntity.setOrderPointCost(point * Constants.VND_PER_POINT);

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() - point);

        orderRepository.save(orderEntity);
        customerRepository.save(customerEntity);
    }

    public void assignPoint(CustomerUsePointDTO payload) {
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
        assignPoint(orderEntity, payload.getPoint());
    }

    @Transactional
    public void rollbackPoint(OrderEntity orderEntity) {
        if (orderEntity.getOrderCustomerPhone() == null) return;
        if (orderEntity.getOrderPointValue() == 0) return;

        CustomerEntity customerEntity = customerRepository.findOneByUserPhone(orderEntity.getOrderCustomerPhone())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        customerEntity.setCustomerPoint(customerEntity.getCustomerPoint() + orderEntity.getOrderPointValue());
        orderEntity.setOrderPointValue(0);
        orderEntity.setOrderPointCost(0);
        orderRepository.save(orderEntity);
        customerRepository.save(customerEntity);
    }

    public void usePoint(OrderEntity orderEntity) {
        if (orderEntity.getOrderCustomerPhone() == null) return;

        if (orderEntity.getOrderPointValue() == 0) return;

        UserEntity userEntity = userRepository.findOneByUserPhone(orderEntity.getOrderCustomerPhone())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        CustomerPointLogEntity pointLogEntity = new CustomerPointLogEntity();
        pointLogEntity.setOrderGuid(orderEntity.getGuid());
        pointLogEntity.setUserGuid(userEntity.getGuid());
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
