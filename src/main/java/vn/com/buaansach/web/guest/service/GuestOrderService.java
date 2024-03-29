package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.*;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.web.guest.repository.order.GuestOrderProductRepository;
import vn.com.buaansach.web.guest.repository.order.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.store.GuestAreaRepository;
import vn.com.buaansach.web.guest.repository.store.GuestSeatRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreRepository;
import vn.com.buaansach.web.guest.repository.user.GuestUserRepository;
import vn.com.buaansach.web.guest.security.GuestStoreSecurity;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCreateOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestOrderUpdateDTO;
import vn.com.buaansach.web.guest.service.mapper.GuestOrderProductMapper;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.shared.service.CodeService;
import vn.com.buaansach.web.shared.service.PriceService;
import vn.com.buaansach.web.shared.service.SaleService;
import vn.com.buaansach.web.shared.service.SeatIdentityService;
import vn.com.buaansach.web.shared.service.dto.readwrite.StoreNotificationDTO;

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
    private final GuestOrderProductMapper guestOrderProductMapper;
    private final PriceService priceService;
    private final SaleService saleService;
    private final CodeService codeService;
    private final SeatIdentityService seatIdentityService;

    public GuestOrderDTO getOrder(String orderGuid) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        result.setListOrderProduct(guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }

    @Transactional
    public GuestOrderDTO createOrder(GuestCreateOrderDTO payload, String currentUser) {
        /* Check list order product */
        if (payload.getListOrderProduct().size() == 0)
            throw new BadRequestException(ErrorCode.LIST_ORDER_PRODUCT_EMPTY);

        /* Check store */
        StoreEntity storeEntity = guestStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        guestStoreSecurity.blockAccessIfStoreAbnormal(storeEntity);

        /* Check seat */
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        guestStoreSecurity.blockAccessIfSeatAbnormal(seatEntity);

        /* Check area */
        AreaEntity areaEntity = guestAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));
        guestStoreSecurity.blockAccessIfAreaAbnormal(areaEntity);

        /* Create order entity */
        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(codeService.generateCodeForOrder(storeEntity));
        orderEntity.setOrderStatus(OrderStatus.CREATED);
        orderEntity.setOrderType(OrderType.valueOf(areaEntity.getAreaType().name()));
        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderTimelineStatus.CREATED, currentUser));
        orderEntity.setSeatGuid(payload.getSeatGuid());
        orderEntity.setStoreGuid(storeEntity.getGuid());
        orderEntity = guestOrderRepository.save(orderEntity);

        /* Update order product */
        UUID orderProductGroup = updateOrderProduct(payload.getListOrderProduct(), storeEntity.getGuid(), orderGuid);
        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.UPDATE_ORDER,
                currentUser,
                payload.getListOrderProduct().size() + "*" + orderProductGroup.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        List<GuestOrderProductDTO> listOrderProductDTO = guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid());
        int totalAmount = priceService.calculateTotalAmount(guestOrderProductMapper.listDtoToListEntity(listOrderProductDTO));
        orderEntity.setOrderTotalAmount(totalAmount);
        orderEntity = guestOrderRepository.save(orderEntity);

        /* Auto apply sale if valid */
        if (storeEntity.getStorePrimarySaleGuid() != null && storeEntity.isStoreAutoApplySale()) {
            orderEntity = saleService.autoApplySale(orderEntity, storeEntity.getStorePrimarySaleGuid(), storeEntity.getGuid());
        }

        /* Update seat order & status */
        seatEntity.setOrderGuid(orderGuid);
        guestSeatService.makeSeatServiceUnfinished(seatEntity);
        seatIdentityService.updateSeatIdentity(payload.getSeatIdentity());

        /* Send notification */
        StoreNotificationDTO notificationDTO = guestStoreOrderNotificationService.createStoreOrderUpdateNotification(storeEntity.getGuid(),
                areaEntity.getGuid(),
                seatEntity.getGuid(),
                orderEntity.getGuid(),
                orderProductGroup,
                payload.getListOrderProduct().size());
        guestSocketService.sendUpdateOrderNotification(notificationDTO);

        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        result.setListOrderProduct(listOrderProductDTO);
        return result;
    }

    @Transactional
    public GuestOrderDTO updateOrder(GuestOrderUpdateDTO payload, String currentUser) {
        /* Check list order product */
        if (payload.getListOrderProduct().size() == 0)
            throw new BadRequestException(ErrorCode.LIST_ORDER_PRODUCT_EMPTY);

        /* Check store status */
        StoreEntity storeEntity = guestStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        guestStoreSecurity.blockAccessIfStoreAbnormal(storeEntity.getGuid());

        /* Check order status */
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        if (orderEntity.getOrderStatus().equals(OrderStatus.PURCHASED))
            throw new BadRequestException(ErrorCode.ORDER_PURCHASED);

        if (orderEntity.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new BadRequestException(ErrorCode.ORDER_CANCELLED);

        /* Check seat */
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        if (seatEntity.isSeatLocked())
            throw new BadRequestException(ErrorCode.SEAT_LOCKED);

        if (!orderEntity.getGuid().equals(seatEntity.getOrderGuid()))
            throw new BadRequestException(ErrorCode.ORDER_AND_SEAT_NOT_MATCH);

        /* Check area */
        AreaEntity areaEntity = guestAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));
        guestStoreSecurity.blockAccessIfAreaAbnormal(areaEntity);

        UUID orderProductGroup = updateOrderProduct(payload.getListOrderProduct(), payload.getStoreGuid(), payload.getOrderGuid());
        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.UPDATE_ORDER,
                currentUser,
                payload.getListOrderProduct().size() + "*" + orderProductGroup.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        List<GuestOrderProductDTO> listOrderProductDTO = guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid());
        int totalAmount = priceService.calculateTotalAmount(guestOrderProductMapper.listDtoToListEntity(listOrderProductDTO));
        orderEntity.setOrderTotalAmount(totalAmount);
        orderEntity = guestOrderRepository.save(orderEntity);

        /* Update seat status */
        guestSeatService.makeSeatServiceUnfinished(seatEntity);

        /* Send notification */
        StoreNotificationDTO notificationDTO = guestStoreOrderNotificationService.createStoreOrderUpdateNotification(storeEntity.getGuid(),
                areaEntity.getGuid(),
                seatEntity.getGuid(),
                orderEntity.getGuid(),
                orderProductGroup,
                payload.getListOrderProduct().size());
        guestSocketService.sendUpdateOrderNotification(notificationDTO);

        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        result.setListOrderProduct(listOrderProductDTO);
        return result;
    }

    public UUID updateOrderProduct(List<GuestOrderProductDTO> listOrderProduct, UUID storeGuid, UUID orderGuid){
        /* Check store product availability */
        List<UUID> listProductGuid = listOrderProduct.stream().map(GuestOrderProductDTO::getProductGuid).collect(Collectors.toList());
        List<StoreProductEntity> listStoreProduct = guestStoreProductRepository.findByStoreGuidAndProductGuidIn(storeGuid, listProductGuid);

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
        guestOrderProductService.saveListOrderProduct(orderProductGroup, orderGuid, listOrderProduct, SecurityUtils.getCurrentUserLogin());
        return orderProductGroup;
    }
}
