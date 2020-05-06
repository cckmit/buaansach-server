package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.OrderCodeGenerator;
import vn.com.buaansach.web.guest.exception.GuestBadRequestException;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestOrderProductRepository;
import vn.com.buaansach.web.guest.repository.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.GuestSeatRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCancelOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestCreateOrderDTO;
import vn.com.buaansach.web.guest.service.dto.write.GuestOrderUpdateDTO;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class GuestOrderService {
    private final GuestOrderRepository guestOrderRepository;
    private final GuestSeatRepository guestSeatRepository;
    private final GuestOrderProductRepository guestOrderProductRepository;
    private final GuestOrderProductService guestOrderProductService;
    private final GuestSocketService guestSocketService;
    private final GuestStoreRepository guestStoreRepository;

    public GuestOrderService(GuestOrderRepository guestOrderRepository, GuestSeatRepository guestSeatRepository, GuestOrderProductRepository guestOrderProductRepository, GuestOrderProductService guestOrderProductService, GuestSocketService guestSocketService, GuestStoreRepository guestStoreRepository) {
        this.guestOrderRepository = guestOrderRepository;
        this.guestSeatRepository = guestSeatRepository;
        this.guestOrderProductRepository = guestOrderProductRepository;
        this.guestOrderProductService = guestOrderProductService;
        this.guestSocketService = guestSocketService;
        this.guestStoreRepository = guestStoreRepository;
    }

    public GuestOrderDTO getOrder(String orderGuid, String seatGuid) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(UUID.fromString(orderGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("order@" + orderGuid));

        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("seat@" + seatGuid));

        if (seatEntity.getCurrentOrderGuid() != null &&
                !seatEntity.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new GuestBadRequestException("orderNotMatchSeat");

        if (orderEntity.getOrderStatus().toString().contains("CANCELLED") ||
                orderEntity.getOrderStatus().equals(OrderStatus.PURCHASED))
            throw new GuestBadRequestException("orderCancelledOrPurchased");

        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        result.setListOrderProduct(guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }

    @Transactional
    public GuestOrderDTO createOrder(GuestCreateOrderDTO payload) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("notfound.seat"));

        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new GuestBadRequestException("badRequest.seatNotEmpty");

        OrderEntity orderEntity = new OrderEntity();
        UUID orderGuid = UUID.randomUUID();
        orderEntity.setGuid(orderGuid);
        orderEntity.setOrderCode(OrderCodeGenerator.generate());
        orderEntity.setOrderStatus(OrderStatus.CREATED);
        orderEntity.setOrderStatusTimeline(TimelineUtil.initOrderStatus(OrderStatus.CREATED));
        orderEntity.setOrderCheckinTime(Instant.now());
        orderEntity.setCustomerPhone(payload.getCustomerPhone());
        orderEntity.setSeatGuid(payload.getSeatGuid());

        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setCurrentOrderGuid(orderGuid);
        guestSeatRepository.save(seatEntity);

        GuestOrderDTO result = new GuestOrderDTO(guestOrderRepository.save(orderEntity));


        guestSocketService.sendMessage("/topic/pos/" + payload.getStoreGuid(), result);
        return result;
    }

    public GuestOrderDTO updateOrder(GuestOrderUpdateDTO payload) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("notfound.order"));

        guestOrderProductService.saveList(payload.getOrderGuid(), payload.getListOrderProduct());

        GuestOrderDTO result = new GuestOrderDTO(orderEntity);
        result.setListOrderProduct(guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(orderEntity.getGuid()));
        return result;
    }

    public void cancelOrder(GuestCancelOrderDTO payload) {
        if (payload.getCancelReason().isEmpty())
            throw new GuestBadRequestException("badRequest.cancelReasonIsRequired");

        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));
        if (!orderEntity.getOrderStatus().equals(OrderStatus.CREATED))
            throw new BadRequestException("badRequest.cannotCancelOrder");

        orderEntity.setOrderStatus(OrderStatus.CANCELLED_BY_CUSTOMER);
        orderEntity.setOrderCancelReason(payload.getCancelReason());

        String newTimeline = TimelineUtil.appendOrderStatus(orderEntity.getOrderStatusTimeline(),
                OrderStatus.CANCELLED_BY_CUSTOMER);
        orderEntity.setOrderStatusTimeline(newTimeline);

        guestSeatRepository.findOneByGuid(orderEntity.getSeatGuid()).ifPresent(seatEntity -> {
            seatEntity.setSeatStatus(SeatStatus.EMPTY);
            seatEntity.setCurrentOrderGuid(null);
            guestSeatRepository.save(seatEntity);
        });
        guestOrderRepository.save(orderEntity);
    }
}
