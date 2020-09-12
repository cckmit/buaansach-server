package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.*;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.pos.service.PosSaleService;
import vn.com.buaansach.web.shared.service.PriceService;
import vn.com.buaansach.util.sequence.OrderCodeGenerator;
import vn.com.buaansach.web.guest.repository.order.GuestOrderProductRepository;
import vn.com.buaansach.web.guest.repository.order.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.store.GuestAreaRepository;
import vn.com.buaansach.web.guest.repository.store.GuestSeatRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreRepository;
import vn.com.buaansach.web.guest.security.GuestStoreSecurity;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreNotificationDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCreateOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestOrderUpdateDTO;
import vn.com.buaansach.web.guest.service.mapper.GuestOrderProductMapper;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
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
    private final GuestStoreProductRepository guestStoreProductRepository;
    private final GuestAreaRepository guestAreaRepository;
    private final GuestStoreOrderNotificationService guestStoreOrderNotificationService;
    private final PriceService priceService;
    private final GuestOrderProductMapper guestOrderProductMapper;
    private final PosSaleService posSaleService;

    public GuestOrderDTO getOrder(String orderGuid) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        result.setListOrderProduct(guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }

    @Transactional
    public GuestOrderDTO createOrder(GuestCreateOrderDTO payload, String currentUser) {
        StoreEntity storeEntity = guestStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        guestStoreSecurity.blockAccessIfStoreIsNotOpenOrDeactivated(storeEntity.getGuid());

        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        AreaEntity areaEntity = guestAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        if (!areaEntity.isAreaActivated())
            throw new BadRequestException(ErrorCode.AREA_DISABLED);

        if (seatEntity.isSeatLocked())
            throw new BadRequestException(ErrorCode.SEAT_LOCKED);

        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException(ErrorCode.SEAT_NON_EMPTY);

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate(storeEntity.getStoreCode()));
        orderEntity.setOrderStatus(OrderStatus.CREATED);

        /* Order type must be the same area type */
        orderEntity.setOrderType(OrderType.valueOf(areaEntity.getAreaType().name()));
        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderStatus.CREATED, currentUser));
        orderEntity.setOrderCancelReason(null);
        orderEntity.setOrderDiscount(0);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderTotalAmount(0);
        orderEntity.setOrderCustomerPhone(null);

        orderEntity.setSaleGuid(null);
        orderEntity.setVoucherGuid(null);
        orderEntity.setVoucherCode(null);
        orderEntity.setSeatGuid(payload.getSeatGuid());
        orderEntity.setPaymentGuid(null);

        /* Update seat status*/
        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        seatEntity.setOrderGuid(orderGuid);
        guestSeatRepository.save(seatEntity);

        orderEntity = guestOrderRepository.save(orderEntity);
        /* Tự động apply sale nếu có */
        if (storeEntity.getStorePrimarySaleGuid() != null){
            orderEntity = posSaleService.autoApplySale(orderEntity, storeEntity.getStorePrimarySaleGuid(), storeEntity.getGuid());
        }

        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        /* make sure order saved then send notification */

        guestSocketService.sendCreateOrderNotification(payload.getStoreGuid(), result);
        return result;
    }

    @Transactional
    public GuestOrderDTO updateOrder(GuestOrderUpdateDTO payload, String currentUser) {
        /* check order product size */
        if (payload.getListOrderProduct().size() == 0)
            throw new BadRequestException(ErrorCode.LIST_ORDER_PRODUCT_EMPTY);

        /* check store status */
        StoreEntity storeEntity = guestStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        guestStoreSecurity.blockAccessIfStoreIsNotOpenOrDeactivated(storeEntity.getGuid());

        /* check order status */
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if (orderEntity.getOrderStatus().equals(OrderStatus.PURCHASED))
            throw new BadRequestException(ErrorCode.ORDER_PURCHASED);

        if (orderEntity.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new BadRequestException(ErrorCode.ORDER_CANCELLED);

        /* check seat, area locked or not */
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        AreaEntity areaEntity = guestAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        if (!areaEntity.isAreaActivated()) {
            throw new BadRequestException(ErrorCode.AREA_DISABLED);
        }

        if (seatEntity.isSeatLocked())
            throw new BadRequestException(ErrorCode.SEAT_LOCKED);

        /* current order not match with seat order */
        if (!orderEntity.getGuid().equals(seatEntity.getOrderGuid()))
            throw new BadRequestException(ErrorCode.ORDER_AND_SEAT_NOT_MATCH);

        /* check product availability */
        List<UUID> listProductGuid = payload.getListOrderProduct().stream().map(GuestOrderProductDTO::getProductGuid).collect(Collectors.toList());
        List<StoreProductEntity> listStoreProduct = guestStoreProductRepository.findByStoreGuidAndProductGuidIn(payload.getStoreGuid(), listProductGuid);

        List<StoreProductEntity> listStopTrading = listStoreProduct.stream()
                .filter(item -> item.getStoreProductStatus().equals(StoreProductStatus.STOP_TRADING)).collect(Collectors.toList());
        if (listStopTrading.size() > 0) {
            throw new BadRequestException(ErrorCode.STORE_PRODUCT_STOP_TRADING);
        }

        List<StoreProductEntity> listUnavailable = listStoreProduct.stream()
                .filter(item -> item.getStoreProductStatus().equals(StoreProductStatus.UNAVAILABLE)).collect(Collectors.toList());
        if (listUnavailable.size() > 0) {
            throw new BadRequestException(ErrorCode.STORE_PRODUCT_UNAVAILABLE);
        }

        UUID orderProductGroup = UUID.randomUUID();
        guestOrderProductService.saveListOrderProduct(orderProductGroup, payload.getOrderGuid(), payload.getListOrderProduct(), currentUser);

        /* Đổi trạng thái phục vụ của bàn ăn => chưa xong */
        guestSeatService.makeSeatServiceUnfinished(orderEntity.getSeatGuid());

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(),
                "UPDATE_ORDER",
                currentUser,
                payload.getListOrderProduct().size() + "*" + orderProductGroup.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        List<GuestOrderProductDTO> listOrderProductDTO = guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid());
        int totalAmount = priceService.calculateTotalAmount(guestOrderProductMapper.listDtoToListEntity(listOrderProductDTO));
        orderEntity.setOrderTotalAmount(totalAmount);

        GuestOrderDTO result = new GuestOrderDTO(guestOrderRepository.save(orderEntity));
        result.setListOrderProduct(listOrderProductDTO);

        /* Send notification */
        GuestStoreNotificationDTO notificationDTO = guestStoreOrderNotificationService.createStoreOrderUpdateNotification(storeEntity.getGuid(),
                areaEntity.getGuid(),
                seatEntity.getGuid(),
                orderEntity.getGuid(),
                orderProductGroup,
                payload.getListOrderProduct().size());
        guestSocketService.sendUpdateOrderNotification(notificationDTO);

        return result;
    }

    public void updateOrderPhone(OrderEntity entity, String currentUser){
        OrderEntity update = guestOrderRepository.findOneByGuid(entity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        String newTimeline = TimelineUtil.appendCustomOrderStatus(update.getOrderStatusTimeline(), "CHANGE_PHONE", currentUser, entity.getOrderCustomerPhone());
        update.setOrderStatusTimeline(newTimeline);
        update.setOrderCustomerPhone(entity.getOrderCustomerPhone());
        guestOrderRepository.save(update);
    }
}
