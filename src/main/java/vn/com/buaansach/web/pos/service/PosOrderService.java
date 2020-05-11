package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.OrderType;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
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
    private final PosVoucherRepository posVoucherRepository;
    private final PosVoucherCodeRepository posVoucherCodeRepository;

    public PosOrderService(PosOrderRepository posOrderRepository, PosSeatRepository posSeatRepository, PosStoreRepository posStoreRepository, PosPaymentService posPaymentService, PosStoreSecurity posStoreSecurity, PosOrderProductService posOrderProductService, PosOrderProductRepository posOrderProductRepository, PosCustomerService posCustomerService, PosSeatService posSeatService, PosVoucherRepository posVoucherRepository, PosVoucherCodeRepository posVoucherCodeRepository) {
        this.posOrderRepository = posOrderRepository;
        this.posSeatRepository = posSeatRepository;
        this.posStoreRepository = posStoreRepository;
        this.posPaymentService = posPaymentService;
        this.posStoreSecurity = posStoreSecurity;
        this.posOrderProductService = posOrderProductService;
        this.posOrderProductRepository = posOrderProductRepository;
        this.posCustomerService = posCustomerService;
        this.posSeatService = posSeatService;
        this.posVoucherRepository = posVoucherRepository;
        this.posVoucherCodeRepository = posVoucherCodeRepository;
    }

    @Transactional
    public PosOrderDTO createOrder(PosOrderCreateDTO payload, String currentUser) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + payload.getSeatGuid()));

        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException("Complete last order on this seat first");

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + payload.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* create customer if not exist */
        if (payload.getCustomerPhone() != null) {
            posCustomerService.createCustomerByPhone(payload.getCustomerPhone());
        }

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate());
        orderEntity.setOrderStatus(OrderStatus.RECEIVED);
        orderEntity.setOrderType(OrderType.IN_STORE);
        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderStatus.RECEIVED, currentUser));
        orderEntity.setOrderCheckinTime(Instant.now());
        orderEntity.setOrderDiscount(0);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderSaleGuid(null);
        orderEntity.setOrderVoucherCode(null);
        orderEntity.setTotalAmount(0L);
        orderEntity.setCustomerPhone(payload.getCustomerPhone());
        orderEntity.setSeatGuid(payload.getSeatGuid());

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setCurrentOrderGuid(orderGuid);
        posSeatRepository.save(seatEntity);

        return new PosOrderDTO(posOrderRepository.save(orderEntity));
    }

    @Transactional
    public PosOrderDTO updateOrder(PosOrderUpdateDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid((payload.getOrderGuid()))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        posOrderProductService.saveList(payload.getOrderGuid(), payload.getListOrderProduct(), currentUser);
        /* if has new order product => set seat service status to UNFINISHED */
        if (payload.getListOrderProduct().size() > 0) {
            posSeatService.makeSeatServiceUnfinished(orderEntity.getSeatGuid());
        }

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

    private long calculateTotalAmount(List<PosOrderProductDTO> listPosOrderProductDTO) {
        return listPosOrderProductDTO.stream()
                .filter(dto -> !dto.getOrderProductStatus().toString().contains("CANCELLED"))
                .mapToLong(dto -> dto.getOrderProductQuantity() * (dto.getOrderProductPrice() - dto.getOrderProductDiscount())).sum();
    }

    public PosOrderDTO getSeatCurrentOrder(String seatGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + seatGuid));

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

    @Transactional
    public void receiveOrder(String orderGuid, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + orderGuid));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        /* receive only when order status is created */
        if (orderEntity.getOrderStatus().equals(OrderStatus.CREATED)) {
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
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + orderEntity.getSeatGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

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

                payAmount = payAmount > 0 ? payAmount : 0L;

                PaymentEntity paymentEntity = posPaymentService.makeCashPayment(payload.getOrderGuid(),
                        payload.getPaymentNote(), payAmount);
                orderEntity.setPaymentGuid(paymentEntity.getGuid());
                orderEntity.setOrderStatus(OrderStatus.PURCHASED);
                orderEntity.setOrderCheckoutTime(Instant.now());
                orderEntity.setCashierLogin(currentUser);

                String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(),
                        OrderStatus.PURCHASED, currentUser);
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

    @Transactional
    public void cancelOrder(PosOrderCancelDTO payload, String currentUser) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        orderEntity.setOrderStatus(OrderStatus.CANCELLED_BY_EMPLOYEE);
        orderEntity.setOrderCancelReason(payload.getCancelReason());

        String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(),
                OrderStatus.CANCELLED_BY_EMPLOYEE, currentUser);
        orderEntity.setOrderStatusTimeline(newTimeline);

        posSeatService.resetSeat(orderEntity.getSeatGuid());
        posOrderRepository.save(orderEntity);
    }

    @Transactional
    public void changeSeat(PosOrderSeatChangeDTO payload, String currentUser) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getCurrentSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + payload.getCurrentSeatGuid()));

        StoreEntity storeNewSeat = posStoreRepository.findOneBySeatGuid(payload.getNewSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + payload.getNewSeatGuid()));

        if (!storeEntity.getGuid().equals(storeNewSeat.getGuid()))
            throw new BadRequestException("All seat must in same store");

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        SeatEntity currentSeat = posSeatRepository.findOneByGuid(payload.getCurrentSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + payload.getCurrentSeatGuid()));

        SeatEntity newSeat = posSeatRepository.findOneByGuid(payload.getNewSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + payload.getNewSeatGuid()));

        if (currentSeat.getCurrentOrderGuid() == null || !currentSeat.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException("Order guid does not match seat current order guid: " + orderEntity.getGuid());

        if (newSeat.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException("New seat is already in use: " + payload.getNewSeatGuid());

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(), "CHANGE_SEAT", currentUser);
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setSeatGuid(newSeat.getGuid());


        /* update new seat status, service status */
        newSeat.setSeatStatus(SeatStatus.NON_EMPTY);
        newSeat.setSeatServiceStatus(currentSeat.getSeatServiceStatus());
        newSeat.setCurrentOrderGuid(orderEntity.getGuid());
        posSeatRepository.save(newSeat);

        posSeatService.resetSeat(currentSeat);

        posOrderRepository.save(orderEntity);
    }

    @Transactional
    public void changeCustomerPhone(PosOrderCustomerPhoneChangeDTO payload, String currentUser) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + payload.getSeatGuid()));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + payload.getSeatGuid()));

        if (seatEntity.getCurrentOrderGuid() == null || !seatEntity.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException("Order guid does not match seat current order guid: " + payload.getOrderGuid());

        /* if customer phone number not change, just return */
        if (payload.getNewCustomerPhone() == null && orderEntity.getCustomerPhone() == null) return;
        if (payload.getNewCustomerPhone() != null && payload.getNewCustomerPhone().equals(orderEntity.getCustomerPhone()))
            return;

        String newTimeline = TimelineUtil.appendCustomOrderStatus(orderEntity.getOrderStatusTimeline(), "CHANGE_PHONE", currentUser);
        orderEntity.setOrderStatusTimeline(newTimeline);
        orderEntity.setCustomerPhone(payload.getNewCustomerPhone().trim());
        posOrderRepository.save(orderEntity);
        if (payload.getNewCustomerPhone() != null && !payload.getNewCustomerPhone().isBlank())
            posCustomerService.createCustomerByPhone(payload.getNewCustomerPhone().trim());
    }
}
