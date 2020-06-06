package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.OrderType;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.OrderCodeGenerator;
import vn.com.buaansach.web.pos.repository.*;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.*;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosOrderService {
    private final PosOrderRepository posOrderRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreRepository posStoreRepository;
    private final PosPaymentService posPaymentService;
    private final PosStoreSecurity posStoreSecurity;
    private final PosOrderProductService posOrderProductService;
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosCustomerService posCustomerService;
    private final PosSeatService posSeatService;
    private final PosVoucherCodeRepository posVoucherCodeRepository;
    private final PosVoucherCodeService posVoucherCodeService;
    private final PosAreaRepository posAreaRepository;

    @Transactional
    public PosOrderDTO createOrder(PosOrderCreateDTO payload, String currentUser) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@seatNotFound@: " + payload.getSeatGuid()));

        /* Chỗ ngồi chưa được giải phóng */
        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException("pos@seatNonEmpty@" + payload.getSeatGuid());

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@seatNotInAnyStore@ " + payload.getSeatGuid()));

        AreaEntity areaEntity = posAreaRepository.findOneByGuid(seatEntity.getAreaGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@areaNotFound@ " + seatEntity.getAreaGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* Tạo khách hàng nếu nhập SĐT chưa có trong hệ thống */
        if (payload.getCustomerPhone() != null) {
            posCustomerService.createCustomerByPhone(payload.getCustomerPhone());
        }

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate());

        /* Đơn tạo bởi nhân viên sẽ mặc định ở trạng thái RECEIVED */
        orderEntity.setOrderStatus(OrderStatus.RECEIVED);

        /* Dựa theo loại khu vực để xác định loại đơn hàng */
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

        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderStatus.RECEIVED, currentUser));
        orderEntity.setOrderCheckinTime(Instant.now());
        orderEntity.setOrderDiscount(0);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderSaleGuid(null);
        orderEntity.setOrderVoucherCode(null);
        orderEntity.setTotalAmount(0L);
        orderEntity.setCustomerPhone(payload.getCustomerPhone());
        orderEntity.setSeatGuid(payload.getSeatGuid());
        orderEntity.setCashierLogin(currentUser);

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setCurrentOrderGuid(orderGuid);
        posSeatRepository.save(seatEntity);

        return new PosOrderDTO(posOrderRepository.save(orderEntity));
    }

    /**
     * Cập nhật sản phẩm cho đơn hàng
     */
    @Transactional
    public PosOrderDTO updateOrder(PosOrderUpdateDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid((payload.getOrderGuid()))
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + orderEntity.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* Lưu tất cả orderProduct của đơn hàng */
        UUID orderProductGroup = UUID.randomUUID();
        posOrderProductService.saveListOrderProduct(orderProductGroup, payload.getOrderGuid(), payload.getListOrderProduct(), currentUser);

        /* Thông thường validate trên ui thì size sẽ phải lớn hơn 0 mới gọi được, kiểm tra lần nữa cho chắc cốp.
         Khi size > 0 thì trạng thái phục vụ của chỗ sẽ đổi thành UNFINISHED */
        if (payload.getListOrderProduct().size() > 0) {
            posSeatService.makeSeatServiceUnfinished(orderEntity.getSeatGuid());
        }

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(),
                "UPDATE_ORDER",
                currentUser,
                payload.getListOrderProduct().size() + "*" + orderProductGroup.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        List<PosOrderProductDTO> listPosOrderProductDTO = posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(payload.getOrderGuid());
        long totalAmount = calculateTotalAmount(listPosOrderProductDTO);
        orderEntity.setTotalAmount(totalAmount);
        PosOrderDTO result = new PosOrderDTO(posOrderRepository.save(orderEntity));

        if (orderEntity.getOrderVoucherCode() != null && !orderEntity.getOrderVoucherCode().isBlank()) {
            PosVoucherCodeDTO voucherCodeDTO = posVoucherCodeRepository.getPosVoucherCodeDTO(orderEntity.getOrderVoucherCode())
                    .orElse(null);
            result.updateVoucherAttribute(voucherCodeDTO);
        }
        result.setListOrderProduct(listPosOrderProductDTO);
        return result;
    }

    /**
     * Công thức tính giá tiền của hóa đơn. Cần đảm bảo giống với công thức trên UI
     * Giá tiền sẽ tính tất cả sản phẩm (kể cả chưa phục vụ), trừ sản phẩm đã bị hủy
     * Giá 1 Sản phẩm = Số lượng x (Giá bán - Giảm giá)
     */
    private long calculateTotalAmount(List<PosOrderProductDTO> listPosOrderProductDTO) {
        return listPosOrderProductDTO.stream()
                .filter(dto -> !dto.getOrderProductStatus().toString().contains("CANCELLED"))
                .mapToLong(dto -> dto.getOrderProductQuantity() * (dto.getOrderProductPrice() - dto.getOrderProductDiscount())).sum();
    }

    public PosOrderDTO getSeatCurrentOrder(String seatGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + seatGuid));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderEntity orderEntity = posOrderRepository.findSeatCurrentOrder(UUID.fromString(seatGuid))
                .orElse(new OrderEntity());
        PosOrderDTO result = new PosOrderDTO(orderEntity);
        if (orderEntity.getGuid() != null) {
            result.setListOrderProduct(posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        }
        if (orderEntity.getOrderVoucherCode() != null && !orderEntity.getOrderVoucherCode().isBlank()) {
            PosVoucherCodeDTO voucherCodeDTO = posVoucherCodeRepository.getPosVoucherCodeDTO(orderEntity.getOrderVoucherCode())
                    .orElse(null);
            result.updateVoucherAttribute(voucherCodeDTO);
        }
        return result;
    }

//    public PosOrderDTO getOrder(String orderGuid) {
//        OrderEntity orderEntity = posOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + orderGuid));
//
//        PosOrderDTO result = new PosOrderDTO(orderEntity);
//        result.setListOrderProduct(posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(orderEntity.getGuid()));
//        return new PosOrderDTO(orderEntity);
//    }

    /**
     * Nhận đơn hàng do khách gọi
     */
    @Transactional
    public void receiveOrder(String orderGuid, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + orderGuid));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + orderEntity.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* receive only when order status is created */
        if (orderEntity.getOrderStatus().equals(OrderStatus.CREATED)) {
            /* Đặt thuộc tính này để phục vụ việc thống kê theo nhân viên */
            orderEntity.setCashierLogin(currentUser);
            orderEntity.setOrderStatus(OrderStatus.RECEIVED);
            String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(), OrderStatus.RECEIVED, currentUser);
            orderEntity.setOrderStatusTimeline(newTimeline);

            List<OrderProductEntity> orderProductEntityList = posOrderProductRepository.findByOrderGuid(orderEntity.getGuid());
            if (orderProductEntityList.size() == 0) {
                posSeatService.makeSeatServiceFinished(orderEntity.getSeatGuid());
            }

            if (orderProductEntityList.size() > 0) {
                posSeatService.makeSeatServiceUnfinished(orderEntity.getSeatGuid());
            }
            posOrderRepository.save(orderEntity);
        }
    }

    @Transactional
    public void purchaseOrder(PosOrderPurchaseDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@seatNotFound@" + orderEntity.getSeatGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + orderEntity.getSeatGuid()));

        if (!orderEntity.getOrderStatus().equals(OrderStatus.RECEIVED))
            throw new BadRequestException("pos@orderStatusNotValid@" + payload.getOrderGuid());

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        switch (payload.getPaymentMethod()) {
            case CASH:
                long payAmount = orderEntity.getTotalAmount();
                if (orderEntity.getOrderDiscount() > 0) {
                    switch (orderEntity.getOrderDiscountType()) {
                        case VALUE:
                            payAmount = payAmount - orderEntity.getOrderDiscount();
                            break;
                        case PERCENT:
                            payAmount = payAmount - (payAmount * orderEntity.getOrderDiscount() / 100L);
                            break;
                    }
                }

                /* Nếu payAmount < 0 thì vẫn set về 0 */
                payAmount = payAmount > 0 ? payAmount : 0L;

                PaymentEntity paymentEntity = posPaymentService.makeCashPayment(
                        payload.getOrderGuid(),
                        payload.getPaymentNote(),
                        payAmount);
                orderEntity.setPaymentGuid(paymentEntity.getGuid());
                orderEntity.setOrderStatus(OrderStatus.PURCHASED);
                orderEntity.setOrderCheckoutTime(Instant.now());
                orderEntity.setCashierLogin(currentUser);

                String newTimeline = TimelineUtil.appendOrderStatus(
                        orderEntity.getOrderStatusTimeline(),
                        OrderStatus.PURCHASED,
                        currentUser);
                orderEntity.setOrderStatusTimeline(newTimeline);

                /* free current seat */
                posSeatService.resetSeat(seatEntity);

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

    /**
     * Hủy đơn hàng
     */
    @Transactional
    public void cancelOrder(PosOrderCancelDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + orderEntity.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        orderEntity.setOrderStatus(OrderStatus.CANCELLED_BY_EMPLOYEE);
        orderEntity.setOrderCancelReason(payload.getCancelReason());
        orderEntity.setCashierLogin(currentUser);

        String newTimeline = TimelineUtil.appendOrderStatus(
                orderEntity.getOrderStatusTimeline(),
                OrderStatus.CANCELLED_BY_EMPLOYEE,
                currentUser);
        orderEntity.setOrderStatusTimeline(newTimeline);

        posSeatService.resetSeat(orderEntity.getSeatGuid());
        posOrderRepository.save(orderEntity);
    }

    /**
     * Đổi vị trí cho đơn hàng
     */
    @Transactional
    public void changeSeat(PosOrderSeatChangeDTO payload, String currentUser) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getCurrentSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + payload.getCurrentSeatGuid()));

        StoreEntity storeNewSeat = posStoreRepository.findOneBySeatGuid(payload.getNewSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + payload.getNewSeatGuid()));

        /* Nếu 2 vị trí không cùng trong một cửa hàng => không hợp lệ */
        if (!storeEntity.getGuid().equals(storeNewSeat.getGuid()))
            throw new BadRequestException("pos@seatsNotInTheSameStore@" + payload.getCurrentSeatGuid() + ";" + payload.getNewSeatGuid());

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));

        SeatEntity currentSeat = posSeatRepository.findOneByGuid(payload.getCurrentSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@seatNotFound@" + payload.getCurrentSeatGuid()));

        SeatEntity newSeat = posSeatRepository.findOneByGuid(payload.getNewSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@seatNotFound@" + payload.getNewSeatGuid()));

        /* Nếu mã đơn được lưu ở vị trí hiện tại khác với mã đơn gửi từ client (payload) => không hợp lệ */
        if (currentSeat.getCurrentOrderGuid() == null || !currentSeat.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException("pos@orderNotMatchSeat@orderGuid=" + orderEntity.getGuid() + ";seatOrderGuid=" + currentSeat.getCurrentOrderGuid());

        /* Nếu vị trí chuyển tới không trống => không hợp lệ */
        if (newSeat.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException("pos@newSeatNotEmpty@" + payload.getNewSeatGuid());

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(),
                "CHANGE_SEAT",
                currentUser,
                currentSeat.getGuid() + "*" + newSeat.getGuid());
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setSeatGuid(newSeat.getGuid());

        /* Cập nhật trạng thái cho vị trí mới */
        newSeat.setSeatStatus(SeatStatus.NON_EMPTY);
        newSeat.setSeatServiceStatus(currentSeat.getSeatServiceStatus());
        newSeat.setCurrentOrderGuid(orderEntity.getGuid());
        posSeatRepository.save(newSeat);

        /* Giải phóng vị trí hiện tại */
        posSeatService.resetSeat(currentSeat);

        posOrderRepository.save(orderEntity);
    }

    /**
     * Đổi SĐT của đơn hàng
     */
    @Transactional
    public void changeCustomerPhone(PosOrderCustomerPhoneChangeDTO payload, String currentUser) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + payload.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@seatNotFound@" + payload.getSeatGuid()));

        if (seatEntity.getCurrentOrderGuid() == null || !seatEntity.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException("pos@orderNotMatchSeat@orderGuid=" + orderEntity.getGuid() + ";seatOrderGuid=" + seatEntity.getCurrentOrderGuid());

        /* if customer phone number not change, just return */
        if (payload.getNewCustomerPhone() == null && orderEntity.getCustomerPhone() == null) return;
        if (payload.getNewCustomerPhone() != null && payload.getNewCustomerPhone().equals(orderEntity.getCustomerPhone()))
            return;

        if (orderEntity.getOrderVoucherCode() != null) {
            /* if current order has apply voucher code that use for specific phone number => auto cancel voucher for that order */
            posVoucherCodeRepository.findOneByVoucherCode(orderEntity.getOrderVoucherCode()).ifPresent(voucherCodeEntity -> {
                if (voucherCodeEntity.getCustomerPhone() != null) {
                    PosOrderVoucherCodeDTO dto = new PosOrderVoucherCodeDTO();
                    dto.setCustomerPhone(orderEntity.getCustomerPhone());
                    dto.setOrderGuid(orderEntity.getGuid());
                    dto.setVoucherCode(orderEntity.getOrderVoucherCode());
                    posVoucherCodeService.cancelVoucherCode(dto);
                }
            });
        }

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(), "CHANGE_PHONE", currentUser, payload.getNewCustomerPhone());
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setCustomerPhone(payload.getNewCustomerPhone().trim());
        posOrderRepository.save(orderEntity);
    }
}
