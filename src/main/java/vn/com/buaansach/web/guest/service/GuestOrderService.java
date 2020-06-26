package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.*;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.util.sequence.OrderCodeGenerator;
import vn.com.buaansach.web.guest.exception.GuestBadRequestException;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.*;
import vn.com.buaansach.web.guest.security.GuestStoreSecurity;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCreateOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestOrderUpdateDTO;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.guest.websocket.dto.GuestSocketDTO;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestOrderService {
    private final GuestOrderRepository guestOrderRepository;
    private final GuestSeatRepository guestSeatRepository;
    private final GuestOrderProductRepository guestOrderProductRepository;
    private final GuestOrderProductService guestOrderProductService;
    private final GuestSocketService guestSocketService;
    private final GuestStoreRepository guestStoreRepository;
    private final GuestStoreSecurity guestStoreSecurity;
    private final GuestSeatService guestSeatService;
    private final GuestOrderFeedbackRepository guestOrderFeedbackRepository;
    private final GuestCustomerService guestCustomerService;
    private final GuestStoreProductRepository guestStoreProductRepository;
    private final GuestAreaRepository guestAreaRepository;

    public GuestOrderDTO getOrder(String orderGuid) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@orderNotFound@" + orderGuid));
        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        result.setHasFeedback(false);
        guestOrderFeedbackRepository.findOneByOrderGuid(orderEntity.getGuid()).ifPresent(orderFeedbackEntity -> {
            result.setHasFeedback(true);
        });
        result.setListOrderProduct(guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }

    @Transactional
    public GuestOrderDTO createOrder(GuestCreateOrderDTO payload, String currentUser) {
        StoreEntity storeEntity = guestStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@storeNotFound@" + payload.getStoreGuid()));

        guestStoreSecurity.blockAccessIfStoreIsNotOpenOrDeactivated(storeEntity.getGuid());

        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@seatNotFound@" + payload.getSeatGuid()));

        AreaEntity areaEntity = guestAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new ResourceNotFoundException("guest@areaNotFound@ " + seatEntity.getAreaGuid()));

        if (!areaEntity.isAreaActivated()){
            throw new GuestBadRequestException("guest@areaDisabled@" + areaEntity.getGuid());
        }

        if (seatEntity.isSeatLocked())
            throw new GuestBadRequestException("guest@seatLocked@" + payload.getSeatGuid());

        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new GuestBadRequestException("guest@seatNonEmpty@" + payload.getSeatGuid());

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate(storeEntity.getStoreCode()));
        orderEntity.setOrderStatus(OrderStatus.CREATED);

        switch (areaEntity.getAreaType()) {
            case IN_STORE:
                orderEntity.setOrderType(OrderType.IN_STORE);
                break;
            case TAKE_AWAY:
                orderEntity.setOrderType(OrderType.TAKE_AWAY);
                break;
            case ONLINE:
                orderEntity.setOrderType(OrderType.ONLINE);
                break;
            case TEST:
                orderEntity.setOrderType(OrderType.TEST);
                break;
        }

        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderStatus.CREATED, currentUser));
        orderEntity.setOrderCheckinTime(Instant.now());
        orderEntity.setOrderDiscount(0);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderSaleGuid(null);
        orderEntity.setOrderVoucherCode(null);
        orderEntity.setTotalAmount(0L);
        orderEntity.setCustomerPhone(null);
        orderEntity.setSeatGuid(payload.getSeatGuid());

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        seatEntity.setCurrentOrderGuid(orderGuid);
        guestSeatRepository.save(seatEntity);

        GuestOrderDTO result = new GuestOrderDTO(guestOrderRepository.save(orderEntity));

        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketConstants.GUEST_CREATE_ORDER, result.getSeatGuid());
        guestSocketService.sendMessage(WebSocketConstants.TOPIC_POS_PREFIX + payload.getStoreGuid(), socketDTO);
        return result;
    }

    @Transactional
    public GuestOrderDTO updateOrder(GuestOrderUpdateDTO payload, String currentUser) {
        /* check order product size */
        if (payload.getListOrderProduct().size() == 0)
            throw new GuestBadRequestException("guest@listOrderProductEmpty@" + payload.getOrderGuid());

        /* check store status */
        StoreEntity storeEntity = guestStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@storeNotFound@" + payload.getStoreGuid()));
        guestStoreSecurity.blockAccessIfStoreIsNotOpenOrDeactivated(storeEntity.getGuid());

        /* check order status */
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@orderNotFound@" + payload.getOrderGuid()));

        if (orderEntity.getOrderStatus().equals(OrderStatus.PURCHASED))
            throw new GuestBadRequestException("guest@orderPurchased@" + payload.getOrderGuid());

        if (orderEntity.getOrderStatus().toString().contains("CANCELLED"))
            throw new GuestBadRequestException("guest@orderCancelled@" + payload.getOrderGuid());

        /* check seat locked */
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@seatNotFound@" + orderEntity.getSeatGuid()));

        AreaEntity areaEntity = guestAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new ResourceNotFoundException("guest@areaNotFound@ " + seatEntity.getAreaGuid()));

        if (!areaEntity.isAreaActivated()){
            throw new GuestBadRequestException("guest@areaDisabled@" + areaEntity.getGuid());
        }

        if (seatEntity.isSeatLocked())
            throw new GuestBadRequestException("guest@seatLocked@" + seatEntity.getGuid());

        if (!orderEntity.getGuid().equals(seatEntity.getCurrentOrderGuid()))
            throw new GuestBadRequestException("guest@orderNotMatchSeat@orderGuid=" + orderEntity.getGuid() + ";seatOrderGuid=" + seatEntity.getCurrentOrderGuid());

        /* check product availability */
        List<UUID> listProductGuid = payload.getListOrderProduct().stream().map(GuestOrderProductDTO::getProductGuid).collect(Collectors.toList());
        List<StoreProductEntity> listStoreProduct = guestStoreProductRepository.findByStoreGuidAndProductGuidIn(payload.getStoreGuid(), listProductGuid);
        List<StoreProductEntity> listStopTrading = listStoreProduct.stream().filter(item -> item.getStoreProductStatus().equals(StoreProductStatus.STOP_TRADING)).collect(Collectors.toList());
        if (listStopTrading.size() > 0) {
            throw new GuestBadRequestException("guest@storeProductStopTrading@");
        }
        List<StoreProductEntity> listUnavailable = listStoreProduct.stream().filter(item -> item.getStoreProductStatus().equals(StoreProductStatus.UNAVAILABLE)).collect(Collectors.toList());
        if (listUnavailable.size() > 0) {
            throw new GuestBadRequestException("guest@storeProductUnavailable@");
        }

        UUID orderProductGroup = UUID.randomUUID();
        guestOrderProductService.saveListOrderProduct(orderProductGroup, payload.getOrderGuid(), payload.getListOrderProduct(), currentUser);

        guestSeatService.makeSeatServiceUnfinished(orderEntity.getSeatGuid());

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(),
                "UPDATE_ORDER",
                currentUser,
                payload.getListOrderProduct().size() + "*" + orderProductGroup.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        List<GuestOrderProductDTO> listOrderProductDTO = guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid());
        long totalAmount = calculateTotalAmount(listOrderProductDTO);
        orderEntity.setTotalAmount(totalAmount);

        GuestOrderDTO result = new GuestOrderDTO(guestOrderRepository.save(orderEntity));
        result.setListOrderProduct(listOrderProductDTO);

        /* Gửi thông báo tới nhân viên */
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketConstants.GUEST_UPDATE_ORDER, result.getSeatGuid());
        guestSocketService.sendMessage(WebSocketConstants.TOPIC_POS_PREFIX + payload.getStoreGuid(), socketDTO);
        return result;
    }

    private long calculateTotalAmount(List<GuestOrderProductDTO> listPosOrderProductDTO) {
        return listPosOrderProductDTO.stream()
                .filter(dto -> !dto.getOrderProductStatus().toString().contains("CANCELLED"))
                .mapToLong(dto -> dto.getOrderProductQuantity() * (dto.getOrderProductPrice() - dto.getOrderProductDiscount())).sum();
    }

//    public void cancelOrder(GuestCancelOrderDTO payload) {
//        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));
//        if (!orderEntity.getOrderStatus().equals(OrderStatus.CREATED))
//            throw new BadRequestException("badRequest.cannotCancelOrder");
//
//        orderEntity.setOrderStatus(OrderStatus.CANCELLED_BY_CUSTOMER);
//        orderEntity.setOrderCancelReason(payload.getCancelReason());
//
//        String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(),
//                OrderStatus.CANCELLED_BY_CUSTOMER);
//        orderEntity.setOrderStatusTimeline(newTimeline);
//
//        guestSeatRepository.findOneByGuid(orderEntity.getSeatGuid()).ifPresent(seatEntity -> {
//            seatEntity.setSeatStatus(SeatStatus.EMPTY);
//            seatEntity.setCurrentOrderGuid(null);
//            guestSeatRepository.save(seatEntity);
//        });
//        guestOrderRepository.save(orderEntity);
//    }
}
