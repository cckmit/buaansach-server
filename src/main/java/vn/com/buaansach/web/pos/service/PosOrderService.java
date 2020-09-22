package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.*;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.util.sequence.OrderCodeGenerator;
import vn.com.buaansach.web.pos.repository.notification.PosStoreNotificationRepository;
import vn.com.buaansach.web.pos.repository.order.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.order.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.store.PosAreaRepository;
import vn.com.buaansach.web.pos.repository.store.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreProductRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.repository.voucher.PosVoucherCodeRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosListOrderDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreNotificationDTO;
import vn.com.buaansach.web.pos.service.dto.write.*;
import vn.com.buaansach.web.pos.service.mapper.PosOrderProductMapper;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.pos.websocket.dto.PosSocketDTO;
import vn.com.buaansach.web.shared.service.PaymentService;
import vn.com.buaansach.web.shared.service.PriceService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosOrderService {
    private final PosOrderRepository posOrderRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreRepository posStoreRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosOrderProductService posOrderProductService;
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosSeatService posSeatService;
    private final PosVoucherCodeRepository posVoucherCodeRepository;
    private final PosVoucherCodeService posVoucherCodeService;
    private final PosAreaRepository posAreaRepository;
    private final PosSocketService posSocketService;
    private final PosVoucherUsageService posVoucherUsageService;
    private final PosStoreNotificationService posStoreNotificationService;
    private final PosOrderProductMapper posOrderProductMapper;
    private final PriceService priceService;
    private final PaymentService paymentService;
    private final PosStoreNotificationRepository posStoreNotificationRepository;
    private final PosSaleService posSaleService;
    private final PosStoreProductRepository posStoreProductRepository;

    @Transactional
    public PosOrderDTO createOrder(PosOrderCreateDTO payload, String currentUser) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        /* Chỗ ngồi chưa được giải phóng */
        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException(ErrorCode.SEAT_NON_EMPTY);

        if (seatEntity.isSeatLocked()) {
            throw new BadRequestException(ErrorCode.SEAT_LOCKED);
        }

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        AreaEntity areaEntity = posAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        if (!areaEntity.isAreaActivated()) {
            throw new BadRequestException(ErrorCode.AREA_DISABLED);
        }

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate(storeEntity.getStoreCode()));

        /* Đơn tạo bởi nhân viên sẽ mặc định ở trạng thái RECEIVED */
        orderEntity.setOrderStatus(OrderStatus.RECEIVED);

        /* Dựa theo loại khu vực để xác định loại đơn hàng */
        orderEntity.setOrderType(OrderType.valueOf(areaEntity.getAreaType().name()));

        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderTimelineStatus.RECEIVED, currentUser));
        orderEntity.setSeatGuid(payload.getSeatGuid());
        orderEntity.setOrderReceivedBy(currentUser);
        orderEntity.setOrderReceivedDate(Instant.now());

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setOrderGuid(orderGuid);
        posSeatRepository.save(seatEntity);

        orderEntity = posOrderRepository.save(orderEntity);

        /* Tự động apply sale nếu có */
        if (storeEntity.getStorePrimarySaleGuid() != null) {
            orderEntity = posSaleService.autoApplySale(orderEntity, storeEntity.getStorePrimarySaleGuid(), storeEntity.getGuid());
        }

        return new PosOrderDTO(orderEntity);
    }

    /**
     * Cập nhật sản phẩm cho đơn hàng
     */
    @Transactional
    public PosOrderDTO updateOrder(PosOrderUpdateDTO payload, String currentUser) {
        if (payload.getListOrderProduct().size() == 0)
            throw new BadRequestException(ErrorCode.LIST_ORDER_PRODUCT_EMPTY);

        OrderEntity orderEntity = posOrderRepository.findOneByGuid((payload.getOrderGuid()))
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* check product availability */
        List<UUID> listProductGuid = payload.getListOrderProduct().stream().map(PosOrderProductDTO::getProductGuid).collect(Collectors.toList());
        List<StoreProductEntity> listStoreProduct = posStoreProductRepository.findByStoreGuidAndProductGuidIn(storeEntity.getGuid(), listProductGuid);

        List<StoreProductEntity> listStopTrading = listStoreProduct.stream()
                .filter(item -> item.getStoreProductStatus().equals(StoreProductStatus.STOP_TRADING)).collect(Collectors.toList());
        if (listStopTrading.size() > 0) {
            throw new BadRequestException(ErrorCode.STORE_PRODUCT_STOP_TRADING);
        }

        /* Lưu tất cả orderProduct của đơn hàng */
        UUID orderProductGroup = UUID.randomUUID();
        posOrderProductService.saveListOrderProduct(orderProductGroup, payload.getOrderGuid(), payload.getListOrderProduct(), currentUser);

        /* Cập nhật thông tin đơn */
        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.UPDATE_ORDER,
                currentUser,
                payload.getListOrderProduct().size() + "*" + orderProductGroup.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        List<PosOrderProductDTO> listPosOrderProductDTO = posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(payload.getOrderGuid());
        int totalAmount = priceService.calculateTotalAmount(posOrderProductMapper.listDtoToListEntity(listPosOrderProductDTO));
        orderEntity.setOrderTotalAmount(totalAmount);

        PosOrderDTO result = new PosOrderDTO(posOrderRepository.save(orderEntity));
        result.setListOrderProduct(listPosOrderProductDTO);

        /* Cập nhật trang thái ghế */
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        posSeatService.makeSeatServiceUnfinished(seatEntity);

        /* Tạo thông báo */
        PosStoreNotificationDTO notificationDTO = posStoreNotificationService.createStoreOrderNotification(
                storeEntity.getGuid(),
                seatEntity.getAreaGuid(),
                seatEntity.getGuid(),
                orderEntity.getGuid(),
                orderProductGroup,
                payload.getListOrderProduct().size());

        posSocketService.sendUpdateOrderNotification(notificationDTO);

        return result;
    }

    public PosOrderDTO getSeatOrder(String seatGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* if order not found, return an empty object so that api wont catch an error */
        OrderEntity orderEntity = posOrderRepository.findSeatCurrentOrder(UUID.fromString(seatGuid))
                .orElse(new OrderEntity());
        PosOrderDTO result = new PosOrderDTO(orderEntity);
        if (orderEntity.getGuid() != null) {
            result.setListOrderProduct(posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        }
        return result;
    }

    /**
     * Nhận đơn hàng do khách gọi
     */
    @Transactional
    public void receiveOrder(String orderGuid, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* receive only when order status is created */
        if (orderEntity.getOrderStatus().equals(OrderStatus.CREATED)) {
            /* Đặt thuộc tính này để phục vụ việc thống kê theo nhân viên */
            orderEntity.setOrderReceivedBy(currentUser);
            orderEntity.setOrderReceivedDate(Instant.now());
            orderEntity.setOrderStatus(OrderStatus.RECEIVED);
            String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(), OrderTimelineStatus.RECEIVED, currentUser);
            orderEntity.setOrderStatusTimeline(newTimeline);

            List<OrderProductEntity> orderProductEntityList = posOrderProductRepository.findByOrderGuid(orderEntity.getGuid());
            if (orderProductEntityList.size() == 0) {
                posSeatService.makeSeatServiceFinished(orderEntity.getSeatGuid());
            } else {
                posSeatService.makeSeatServiceUnfinished(orderEntity.getSeatGuid());
            }
            posOrderRepository.save(orderEntity);

            /* Gửi thông báo tới khách */
            posSocketService.sendOrderReceivedNotification(orderEntity.getGuid());
        }
    }

    @Transactional
    public void purchaseOrder(PosOrderPurchaseDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        if (!orderEntity.getOrderStatus().equals(OrderStatus.RECEIVED))
            throw new BadRequestException(ErrorCode.INVALID_ORDER_STATUS);

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* create voucher code usage record if a voucher code has been applied */
        if (orderEntity.getVoucherCode() != null && !orderEntity.getVoucherCode().isBlank()) {
            posVoucherUsageService.createVoucherUsage(orderEntity.getVoucherCode(), orderEntity.getGuid(), orderEntity.getOrderCustomerPhone());
        }

        if (orderEntity.getSaleGuid() != null) {
            posSaleService.addSaleUsage(orderEntity.getSaleGuid(), storeEntity.getGuid(), orderEntity.getGuid());
        }

        switch (payload.getPaymentMethod()) {
            case CASH:
                /* Thanh toán tiền mặt */
                PaymentEntity paymentEntity = paymentService.makeCashPayment(payload.getPaymentNote(), orderEntity);

                orderEntity.setPaymentGuid(paymentEntity.getGuid());
                orderEntity.setOrderStatus(OrderStatus.PURCHASED);
                orderEntity.setOrderPurchasedBy(currentUser);
                orderEntity.setOrderPurchasedDate(Instant.now());

                String newTimeline = TimelineUtil.appendOrderStatus(
                        orderEntity.getOrderStatusTimeline(),
                        OrderTimelineStatus.PURCHASED,
                        currentUser);
                orderEntity.setOrderStatusTimeline(newTimeline);

                /* free current seat */
                posSeatService.resetSeat(seatEntity);

                posOrderRepository.save(orderEntity);
                break;
            case VN_PAY:
                break;
            default:
                break;
        }

        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage(WebSocketConstants.POS_PURCHASE_ORDER);
        dto.setPayload(null);
        posSocketService.sendMessage(WebSocketConstants.TOPIC_GUEST_PREFIX + payload.getOrderGuid(), dto);
    }

    /**
     * Hủy đơn hàng
     */
    @Transactional
    public void cancelOrder(PosOrderCancelDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        orderEntity.setOrderStatus(OrderStatus.CANCELLED);
        orderEntity.setOrderCancelReason(payload.getCancelReason());
        orderEntity.setOrderCancelledBy(currentUser);
        orderEntity.setOrderCancelledDate(Instant.now());

        String newTimeline = TimelineUtil.appendOrderStatus(
                orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.CANCELLED,
                currentUser);
        orderEntity.setOrderStatusTimeline(newTimeline);

        posSeatService.resetSeat(orderEntity.getSeatGuid());
        posOrderRepository.save(orderEntity);

        /*Gửi thông báo tới khách*/
        posSocketService.sendOrderCancelledNotification(payload.getOrderGuid());
    }

    /**
     * Đổi vị trí cho đơn hàng
     */
    @Transactional
    public void changeSeat(PosOrderSeatChangeDTO payload, String currentUser) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getCurrentSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        StoreEntity storeNewSeat = posStoreRepository.findOneBySeatGuid(payload.getNewSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        /* Nếu 2 vị trí không cùng trong một cửa hàng => không hợp lệ */
        if (!storeEntity.getGuid().equals(storeNewSeat.getGuid()))
            throw new BadRequestException(ErrorCode.SEAT_NOT_IN_SAME_STORE);

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        SeatEntity currentSeat = posSeatRepository.findOneByGuid(payload.getCurrentSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        SeatEntity newSeat = posSeatRepository.findOneByGuid(payload.getNewSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        /* Nếu mã đơn được lưu ở vị trí hiện tại khác với mã đơn gửi từ client (payload) => không hợp lệ */
        if (currentSeat.getOrderGuid() == null || !currentSeat.getOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException(ErrorCode.ORDER_AND_SEAT_NOT_MATCH);

        /* Nếu vị trí chuyển tới không trống => không hợp lệ */
        if (newSeat.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException(ErrorCode.SEAT_NON_EMPTY);

        /* Cập nhật vị trí mới vào order */
        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.CHANGE_SEAT,
                currentUser,
                currentSeat.getGuid() + "*" + newSeat.getGuid());
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setSeatGuid(newSeat.getGuid());
        posOrderRepository.save(orderEntity);

        /* Cập nhật lại vị trí cho các thông báo gọi món */
        List<StoreNotificationEntity> listNotification = posStoreNotificationRepository.findByOrderGuid(orderEntity.getGuid());
        listNotification = listNotification.stream().peek(item -> {
            item.setSeatGuid(newSeat.getGuid());
            item.setAreaGuid(newSeat.getAreaGuid());
            item.setStoreGuid(storeNewSeat.getGuid());
        }).collect(Collectors.toList());
        posStoreNotificationRepository.saveAll(listNotification);

        /* Cập nhật trạng thái cho vị trí mới */
        newSeat.setSeatStatus(SeatStatus.NON_EMPTY);
        newSeat.setSeatServiceStatus(currentSeat.getSeatServiceStatus());
        newSeat.setOrderGuid(orderEntity.getGuid());
        posSeatRepository.save(newSeat);

        /* Giải phóng vị trí hiện tại */
        posSeatService.resetSeat(currentSeat);

        /* Gửi thông báo tới khách */
        posSocketService.sendSeatChangeNotification(payload.getOrderGuid(), newSeat);
    }

    /**
     * Đổi SĐT của đơn hàng
     */
    @Transactional
    public void changeCustomerPhone(PosOrderCustomerPhoneChangeDTO payload, String currentUser) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        if (seatEntity.getOrderGuid() == null || !seatEntity.getOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException(ErrorCode.ORDER_AND_SEAT_NOT_MATCH);

        /* if customer phone number not change, just return */
        if (payload.getNewCustomerPhone() == null && orderEntity.getOrderCustomerPhone() == null) return;
        if (payload.getNewCustomerPhone() != null && payload.getNewCustomerPhone().equals(orderEntity.getOrderCustomerPhone()))
            return;

        if (orderEntity.getVoucherCode() != null) {
            /* if current order has apply voucher code that use for specific phone number => auto cancel voucher for that order */
            posVoucherCodeRepository.findOneByVoucherCode(orderEntity.getVoucherCode()).ifPresent(voucherCodeEntity -> {
                if (voucherCodeEntity.getVoucherCodePhone() != null) {
                    PosOrderVoucherCodeDTO dto = new PosOrderVoucherCodeDTO();
                    dto.setOrderGuid(orderEntity.getGuid());
                    dto.setCustomerPhone(orderEntity.getOrderCustomerPhone());
                    dto.setVoucherCode(orderEntity.getVoucherCode());
                    posVoucherCodeService.cancelVoucherCode(dto);
                }
            });
        }

        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.UPDATE_PHONE,
                currentUser,
                payload.getNewCustomerPhone());
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setOrderCustomerPhone(payload.getNewCustomerPhone());
        posOrderRepository.save(orderEntity);
    }

    public List<PosOrderDTO> getListOrderByListSeat(PosListOrderDTO payload) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<SeatEntity> listSeat = posSeatRepository.findByGuidIn(payload.getListSeatGuid());

        if (listSeat.stream().anyMatch(item -> item.getSeatServiceStatus().equals(SeatServiceStatus.UNFINISHED)))
            throw new BadRequestException(ErrorCode.LIST_PURCHASE_HAS_UNFINISHED_SEAT);
        List<UUID> listOrderGuid = listSeat.stream().map(SeatEntity::getOrderGuid).collect(Collectors.toList());
        List<OrderEntity> listOrder = posOrderRepository.findByGuidIn(listOrderGuid);
        if (listOrder.size() != payload.getListSeatGuid().size())
            throw new BadRequestException(ErrorCode.ORDER_AND_SEAT_SIZE_NOT_EQUAL);
        List<PosOrderDTO> listPosOrder = listOrder.stream().map(PosOrderDTO::new).collect(Collectors.toList());
        listPosOrder.forEach(item -> {
            item.setListOrderProduct(posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(item.getGuid()));
        });
        return listPosOrder;
    }

    @Transactional
    public void purchaseGroupOrder(PosPurchaseGroupDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        if (payload.getListSeatGuid().size() == 0)
            throw new BadRequestException(ErrorCode.LIST_SEAT_GUID_EMPTY);

        List<SeatEntity> listSeat = posSeatRepository.findByGuidIn(payload.getListSeatGuid());
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(listSeat.get(0).getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        if (listSeat.size() != payload.getListSeatGuid().size())
            throw new BadRequestException(ErrorCode.SOME_ORDER_NOT_FOUND);

        listSeat.forEach(item -> {
            if (item.getSeatStatus().equals(SeatStatus.EMPTY))
                throw new BadRequestException(ErrorCode.LIST_PURCHASE_HAS_EMPTY_SEAT);
            if (item.getSeatServiceStatus().equals(SeatServiceStatus.UNFINISHED))
                throw new BadRequestException(ErrorCode.LIST_PURCHASE_HAS_UNFINISHED_SEAT);
        });

        List<UUID> listOrderGuid = listSeat.stream().map(SeatEntity::getOrderGuid).collect(Collectors.toList());

        List<OrderEntity> listOrder = posOrderRepository.findByGuidIn(listOrderGuid);

        if (listOrder.size() != payload.getListSeatGuid().size())
            throw new BadRequestException(ErrorCode.ORDER_AND_SEAT_SIZE_NOT_EQUAL);

        PaymentEntity payment = paymentService.makeCashPayment(payload.getPaymentNote(), listOrder);
        listOrder.forEach(orderEntity -> {
            orderEntity.setPaymentGuid(payment.getGuid());
            orderEntity.setOrderStatus(OrderStatus.PURCHASED);
            orderEntity.setOrderPurchasedBy(currentUser);
            orderEntity.setOrderPurchasedDate(Instant.now());
            if (orderEntity.getSaleGuid() != null) {
                posSaleService.addSaleUsage(orderEntity.getSaleGuid(), storeEntity.getGuid(), orderEntity.getGuid());
            }

            String newTimeline = TimelineUtil.appendOrderStatus(
                    orderEntity.getOrderStatusTimeline(),
                    OrderTimelineStatus.PURCHASED,
                    currentUser);
            orderEntity.setOrderStatusTimeline(newTimeline);
        });

        posOrderRepository.saveAll(listOrder);
        posSeatService.resetListSeat(listSeat);
    }
}
