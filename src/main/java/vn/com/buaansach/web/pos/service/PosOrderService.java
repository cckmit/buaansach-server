package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.OrderCodeGenerator;
import vn.com.buaansach.web.admin.service.StoreSecurityService;
import vn.com.buaansach.web.pos.repository.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;
import vn.com.buaansach.web.pos.service.dto.write.*;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PosOrderService {
    private final PosOrderRepository posOrderRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreRepository posStoreRepository;
    private final PosPaymentService posPaymentService;
    private final StoreSecurityService storeSecurityService;
    private final PosOrderProductService posOrderProductService;
    private final PosOrderProductRepository posOrderProductRepository;

    public PosOrderService(PosOrderRepository posOrderRepository, PosSeatRepository posSeatRepository, PosStoreRepository posStoreRepository, PosPaymentService posPaymentService, StoreSecurityService storeSecurityService, PosOrderProductService posOrderProductService, PosOrderProductRepository posOrderProductRepository) {
        this.posOrderRepository = posOrderRepository;
        this.posSeatRepository = posSeatRepository;
        this.posStoreRepository = posStoreRepository;
        this.posPaymentService = posPaymentService;
        this.storeSecurityService = storeSecurityService;
        this.posOrderProductService = posOrderProductService;
        this.posOrderProductRepository = posOrderProductRepository;
    }

    @Transactional
    public PosOrderDTO createOrder(PosOrderCreateDTO payload, String currentUser) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + payload.getSeatGuid()));

        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException("Please complete last order on this seat first");

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + payload.getSeatGuid()));

        storeSecurityService.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate());
        orderEntity.setOrderStatus(OrderStatus.RECEIVED);
        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderStatus.RECEIVED, currentUser));
        orderEntity.setOrderCheckinTime(Instant.now());
        orderEntity.setCustomerPhone(payload.getCustomerPhone());
        orderEntity.setSeatGuid(payload.getSeatGuid());

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setCurrentOrderGuid(orderGuid);
        return new PosOrderDTO(posOrderRepository.save(orderEntity));
    }


    @Transactional
    public PosOrderDTO updateOrder(PosOrderUpdateDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid((payload.getOrderGuid()))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

        storeSecurityService.blockAccessIfNotInStore(storeEntity.getGuid());

        orderEntity.setCustomerPhone(payload.getCustomerPhone());
        posOrderRepository.save(orderEntity);

        posOrderProductService.saveList(payload.getOrderGuid(), payload.getListOrderProduct(), currentUser);
        PosOrderDTO result = new PosOrderDTO(orderEntity);
        result.setListOrderProduct(posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(payload.getOrderGuid()));
        return result;
    }

    public PosOrderDTO getOrderBySeatGuid(String seatGuid) {
        OrderEntity orderEntity = posOrderRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElse(new OrderEntity());
        PosOrderDTO result = new PosOrderDTO(orderEntity);
        if (orderEntity.getGuid() != null) {
            result.setListOrderProduct(posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        }
        return result;
    }

    public PosOrderDTO getOrder(String orderGuid) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + orderGuid));
        PosOrderDTO result = new PosOrderDTO(orderEntity);
        result.setListOrderProduct(posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return new PosOrderDTO(orderEntity);
    }

    @Transactional
    public void receiveOrder(String orderGuid, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + orderGuid));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

        storeSecurityService.blockAccessIfNotInStore(storeEntity.getGuid());

        /* receive only when order status is created */
        if (orderEntity.getOrderStatus().equals(OrderStatus.CREATED)) {
            orderEntity.setOrderStatus(OrderStatus.RECEIVED);
            String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(), OrderStatus.RECEIVED, currentUser);
            orderEntity.setOrderStatusTimeline(newTimeline);

            /* switch all order store product from CREATED => PREPARING */
            List<OrderProductEntity> orderProductEntityList = posOrderProductRepository.findByOrderGuid(orderEntity.getGuid());
            orderProductEntityList = orderProductEntityList.stream().peek(orderProductEntity -> {
                orderProductEntity.setOrderProductStatus(OrderProductStatus.PREPARING);
                String timeline = TimelineUtil.appendOrderProductStatus(orderProductEntity.getOrderProductStatusTimeline(), OrderProductStatus.PREPARING, currentUser);
                orderProductEntity.setOrderProductStatusTimeline(timeline);
            }).collect(Collectors.toList());

            posOrderProductRepository.saveAll(orderProductEntityList);
            posOrderRepository.save(orderEntity);
        }
    }

    @Transactional
    public void purchaseOrder(PosOrderPurchaseDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + orderEntity.getSeatGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

        storeSecurityService.blockAccessIfNotInStore(storeEntity.getGuid());

//        List<OrderProductEntity> orderProductEntityList = posOrderProductRepository.findByOrderGuid(orderEntity.getGuid());
//        long totalCharge = orderProductEntityList.stream().mapToLong(OrderProductEntity::getOrderProductPrice).sum();

        switch (payload.getPaymentMethod()) {
            case CASH:
                PaymentEntity paymentEntity = posPaymentService.makeCashPayment(payload.getOrderGuid(), payload.getTotalCharge());
                orderEntity.setPaymentGuid(paymentEntity.getGuid());
                orderEntity.setOrderStatus(OrderStatus.PURCHASED);
                orderEntity.setOrderCheckoutTime(Instant.now());

                String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(),
                        OrderStatus.PURCHASED, currentUser);
                orderEntity.setOrderStatusTimeline(newTimeline);

                /* free current seat */
                seatEntity.setSeatStatus(SeatStatus.EMPTY);
                seatEntity.setCurrentOrderGuid(null);
                posSeatRepository.save(seatEntity);

                posOrderRepository.save(orderEntity);
                break;
            case VN_PAY:
            case VIETTEL_PAY:
            case CREDIT_CARD:
            case ZALO_PAY:
            case MOMO_APP:
            default:
                break;
        }
    }

    @Transactional
    public void cancelOrder(PosOrderCancelDTO payload, String currentUser) {
        if (payload.getCancelReason().isEmpty()) throw new BadRequestException("Cancel Reason is required");

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

        storeSecurityService.blockAccessIfNotInStore(storeEntity.getGuid());

        orderEntity.setOrderStatus(OrderStatus.CANCELLED_BY_EMPLOYEE);
        orderEntity.setOrderCancelReason(payload.getCancelReason());

        String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(),
                OrderStatus.CANCELLED_BY_EMPLOYEE, currentUser);
        orderEntity.setOrderStatusTimeline(newTimeline);

        posSeatRepository.findOneByGuid(orderEntity.getSeatGuid()).ifPresent(seatEntity -> {
            seatEntity.setSeatStatus(SeatStatus.EMPTY);
            seatEntity.setCurrentOrderGuid(null);
            posSeatRepository.save(seatEntity);
        });
        posOrderRepository.save(orderEntity);
    }

    @Transactional
    public void changeSeat(PosOrderSeatChangeDTO payload, String currentUser) {
        storeSecurityService.blockAccessIfNotInStore(payload.getStoreGuid());

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        SeatEntity currentSeat = posSeatRepository.findOneByGuid(payload.getCurrentSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + payload.getCurrentSeatGuid()));

        SeatEntity newSeat = posSeatRepository.findOneByGuid(payload.getNewSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + payload.getNewSeatGuid()));

        if (currentSeat.getCurrentOrderGuid() == null || !currentSeat.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException("Order guid not match current seat guid: " + payload.getCurrentSeatGuid());

        if (newSeat.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException("New seat is not empty: " + payload.getNewSeatGuid());

        String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(), "CHANGE_SEAT", currentUser);
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setSeatGuid(newSeat.getGuid());

        currentSeat.setSeatStatus(SeatStatus.EMPTY);
        currentSeat.setCurrentOrderGuid(null);

        newSeat.setSeatStatus(SeatStatus.NON_EMPTY);
        newSeat.setCurrentOrderGuid(orderEntity.getGuid());

        posSeatRepository.save(currentSeat);
        posSeatRepository.save(newSeat);
        posOrderRepository.save(orderEntity);
    }
}
