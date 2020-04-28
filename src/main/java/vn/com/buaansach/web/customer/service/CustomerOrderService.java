package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.OrderCodeGenerator;
import vn.com.buaansach.web.customer.exception.CustomerBadRequestException;
import vn.com.buaansach.web.customer.exception.CustomerResourceNotFoundException;
import vn.com.buaansach.web.customer.repository.CustomerOrderProductRepository;
import vn.com.buaansach.web.customer.repository.CustomerOrderRepository;
import vn.com.buaansach.web.customer.repository.CustomerSeatRepository;
import vn.com.buaansach.web.customer.service.dto.readwrite.CustomerOrderDTO;
import vn.com.buaansach.web.customer.service.dto.write.CustomerCancelOrderDTO;
import vn.com.buaansach.web.customer.service.dto.write.CustomerOrderUpdateDTO;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerSeatRepository customerSeatRepository;
    private final CustomerOrderProductRepository customerOrderProductRepository;
    private final CustomerOrderProductService customerOrderProductService;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository, CustomerSeatRepository customerSeatRepository, CustomerOrderProductRepository customerOrderProductRepository, CustomerOrderProductService customerOrderProductService) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerSeatRepository = customerSeatRepository;
        this.customerOrderProductRepository = customerOrderProductRepository;
        this.customerOrderProductService = customerOrderProductService;
    }

    public CustomerOrderDTO getOrder(String orderGuid, String seatGuid) {
        OrderEntity orderEntity = customerOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new CustomerResourceNotFoundException("notfound.order"));

        SeatEntity seatEntity = customerSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new CustomerResourceNotFoundException("notfound.seat"));

        if (seatEntity.getCurrentOrderGuid() != null && !seatEntity.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new CustomerBadRequestException("badRequest.orderNotMatchSeat");
        if (orderEntity.getOrderStatus().toString().contains("CANCELLED")) return new CustomerOrderDTO();
        CustomerOrderDTO result = new CustomerOrderDTO(orderEntity);
        result.setListOrderProduct(customerOrderProductRepository.findListCustomerOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }

    @Transactional
    public CustomerOrderDTO createOrder(String seatGuid) {
        SeatEntity seatEntity = customerSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new CustomerResourceNotFoundException("notfound.seat"));

        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new CustomerBadRequestException("badRequest.seatNotEmpty");

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate());
        orderEntity.setOrderStatus(OrderStatus.CREATED);
        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderStatus.CREATED));
        orderEntity.setOrderCheckinTime(Instant.now());
        orderEntity.setSeatGuid(UUID.fromString(seatGuid));

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setCurrentOrderGuid(orderGuid);
        customerSeatRepository.save(seatEntity);
        return new CustomerOrderDTO(customerOrderRepository.save(orderEntity));
    }

    public CustomerOrderDTO updateOrder(CustomerOrderUpdateDTO payload) {
        OrderEntity orderEntity = customerOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new CustomerResourceNotFoundException("notfound.order"));

        customerOrderProductService.saveList(payload.getOrderGuid(), payload.getListOrderProduct());

        CustomerOrderDTO result = new CustomerOrderDTO(orderEntity);
        result.setListOrderProduct(customerOrderProductRepository.findListCustomerOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }

    public void cancelOrder(CustomerCancelOrderDTO payload) {
        if (payload.getCancelReason().isEmpty())
            throw new CustomerBadRequestException("badRequest.cancelReasonIsRequired");

        OrderEntity orderEntity = customerOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));
        if (!orderEntity.getOrderStatus().equals(OrderStatus.CREATED))
            throw new BadRequestException("badRequest.cannotCancelOrder");

        orderEntity.setOrderStatus(OrderStatus.CANCELLED_BY_CUSTOMER);
        orderEntity.setOrderCancelReason(payload.getCancelReason());

        String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(),
                OrderStatus.CANCELLED_BY_CUSTOMER);
        orderEntity.setOrderStatusTimeline(newTimeline);

        customerSeatRepository.findOneByGuid(orderEntity.getSeatGuid()).ifPresent(seatEntity -> {
            seatEntity.setSeatStatus(SeatStatus.EMPTY);
            seatEntity.setCurrentOrderGuid(null);
            customerSeatRepository.save(seatEntity);
        });
        customerOrderRepository.save(orderEntity);
    }
}
