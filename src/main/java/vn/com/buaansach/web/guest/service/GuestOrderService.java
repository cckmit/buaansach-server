package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.*;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.*;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
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
    private final GuestStoreProductRepository guestStoreProductRepository;
    private final GuestAreaRepository guestAreaRepository;
    private final GuestStoreOrderService guestStoreOrderService;
    private final GuestCustomerRepository guestCustomerRepository;

    public GuestOrderDTO getOrder(String orderGuid) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@orderNotFound@" + orderGuid));
        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
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
                .orElseThrow(() -> new NotFoundException("guest@areaNotFound@ " + seatEntity.getAreaGuid()));

        if (!areaEntity.isAreaActivated()) {
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
        orderEntity.setOrderDiscount(0);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setSaleGuid(null);
        orderEntity.setVoucherCode(null);
        orderEntity.setOrderTotalAmount(0);
        orderEntity.setOrderCustomerPhone(null);
        orderEntity.setSeatGuid(payload.getSeatGuid());

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        seatEntity.setOrderGuid(orderGuid);
        guestSeatRepository.save(seatEntity);

        GuestOrderDTO result = new GuestOrderDTO(guestOrderRepository.save(orderEntity));

        /* just set seat guid for same payload received as update order */
        StoreOrderEntity storeOrderEntity = new StoreOrderEntity();
        storeOrderEntity.setSeatGuid(result.getSeatGuid());
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketConstants.GUEST_CREATE_ORDER, storeOrderEntity);
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

        /* check seat, area locked or not */
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException(ErrorCode.SEAT_NOT_FOUND));

        AreaEntity areaEntity = guestAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        if (!areaEntity.isAreaActivated()) {
            throw new GuestBadRequestException("guest@areaDisabled@" + areaEntity.getGuid());
        }

        if (seatEntity.isSeatLocked())
            throw new GuestBadRequestException("guest@seatLocked@" + seatEntity.getGuid());

        /* current order not match with seat order */
        if (!orderEntity.getGuid().equals(seatEntity.getOrderGuid()))
            throw new GuestBadRequestException("guest@orderNotMatchSeat@orderGuid=" + orderEntity.getGuid() + ";seatOrderGuid=" + seatEntity.getOrderGuid());

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

        /* Tạo thông báo */
        StoreOrderEntity storeOrderEntity = guestStoreOrderService.createStoreOrder(
                storeEntity.getGuid(),
                seatEntity.getAreaGuid(),
                seatEntity.getGuid(),
                orderEntity.getGuid(),
                orderProductGroup,
                payload.getListOrderProduct().size());

        /* Gửi thông báo tới nhân viên */
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketConstants.GUEST_UPDATE_ORDER, storeOrderEntity);
        guestSocketService.sendMessage(WebSocketConstants.TOPIC_POS_PREFIX + payload.getStoreGuid(), socketDTO);

        /* Đổi trạng thái phục vụ của bàn ăn => chưa xong */
        guestSeatService.makeSeatServiceUnfinished(orderEntity.getSeatGuid());

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(),
                "UPDATE_ORDER",
                currentUser,
                payload.getListOrderProduct().size() + "*" + orderProductGroup.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        List<GuestOrderProductDTO> listOrderProductDTO = guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid());
        int totalAmount = calculateTotalAmount(listOrderProductDTO);
        orderEntity.setOrderTotalAmount(totalAmount);

        GuestOrderDTO result = new GuestOrderDTO(guestOrderRepository.save(orderEntity));
        result.setListOrderProduct(listOrderProductDTO);
        return result;
    }

    private int calculateTotalAmount(List<GuestOrderProductDTO> listPosOrderProductDTO) {
        return listPosOrderProductDTO.stream()
                .filter(dto -> !dto.getOrderProductStatus().toString().contains("CANCELLED"))
                .mapToInt(dto -> dto.getOrderProductQuantity() * (dto.getOrderProductPrice() - dto.getOrderProductDiscount())).sum();
    }

    @Transactional
    public OrderEntity updateCustomerPhone(UUID orderGuid, String customerPhone) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(orderGuid)
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@orderNotFound@" + orderGuid));

        /* Không cho phép khách thay đổi SDT nếu đơn đã có SDT */
        if (orderEntity.getOrderCustomerPhone() != null)
            throw new GuestBadRequestException("guest@orderCustomerPhoneExist@" + orderEntity.getGuid());

        /* Nếu SDT chưa tồn tại trên hệ thống */
        if (guestCustomerRepository.findOneByCustomerPhone(customerPhone).isEmpty())
            throw new GuestBadRequestException("guest@customerPhoneNotFound@" + customerPhone);

        String newTimeline = TimelineUtil.appendCustomOrderStatus(
                orderEntity.getOrderStatusTimeline(),
                "CHANGE_PHONE",
                SecurityUtils.getCurrentUserLogin(),
                customerPhone);
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setOrderCustomerPhone(customerPhone);
        return guestOrderRepository.save(orderEntity);
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
