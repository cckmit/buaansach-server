package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.guest.repository.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.GuestSeatRepository;
import vn.com.buaansach.web.guest.repository.GuestStorePayRequestRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestStorePayRequestDTO;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.guest.websocket.dto.GuestSocketDTO;
import vn.com.buaansach.web.pos.service.PosPaymentService;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStorePayRequestService {
    private final GuestStorePayRequestRepository guestStorePayRequestRepository;
    private final GuestOrderRepository guestOrderRepository;
    private final GuestStoreRepository guestStoreRepository;
    private final GuestSeatRepository guestSeatRepository;
    private final GuestSocketService guestSocketService;
    private final PosPaymentService posPaymentService;

    @Transactional
    public GuestStorePayRequestDTO sendRequest(GuestStorePayRequestDTO payload) {
        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException("guest@storeNotFoundWithSeat@" + payload.getSeatGuid()));
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException("guest@seatNotFound@" + payload.getSeatGuid()));
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException("guest@orderNotFound@" + payload.getOrderGuid()));

        if (guestStorePayRequestRepository.findOneByOrderGuid(orderEntity.getGuid()).isPresent())
            throw new BadRequestException("guest@storePayRequestExistWithOrderGuid@" + orderEntity.getGuid());

        long payAmount = posPaymentService.calculatePayAmount(orderEntity);
        if (payAmount > payload.getStorePayRequestAmount())
            throw new BadRequestException("guest@payAmountNotEnough@" + payAmount);

        if (!seatEntity.getOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException("guest@orderNotMatchSeat@orderGuid=" + orderEntity.getGuid() + ";seatOrderGuid=" + seatEntity.getOrderGuid());

        StorePayRequestNotificationEntity storePayRequestNotificationEntity = new StorePayRequestNotificationEntity();
//        storePayRequestNotificationEntity.setGuid(UUID.randomUUID());
//        storePayRequestNotificationEntity.setStorePayRequestStatus(StorePayRequestStatus.UNSEEN);
//        storePayRequestNotificationEntity.setStorePayRequestAmount(payload.getStorePayRequestAmount());
//        storePayRequestNotificationEntity.setStorePayRequestNote(payload.getStorePayRequestNote());
//
//        storePayRequestNotificationEntity.setStoreGuid(storeEntity.getGuid());
//        storePayRequestNotificationEntity.setAreaGuid(seatEntity.getAreaGuid());
//        storePayRequestNotificationEntity.setSeatGuid(seatEntity.getGuid());
//        storePayRequestNotificationEntity.setOrderGuid(orderEntity.getGuid());
        StorePayRequestNotificationEntity result = guestStorePayRequestRepository.save(storePayRequestNotificationEntity);

        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketConstants.GUEST_STORE_PAY_REQUEST, storePayRequestNotificationEntity);
        guestSocketService.sendMessage(WebSocketConstants.TOPIC_POS_PREFIX + storeEntity.getGuid(), socketDTO);
        return new GuestStorePayRequestDTO(result);
    }

    public GuestStorePayRequestDTO getByOrderGuid(String orderGuid) {
        return new GuestStorePayRequestDTO(guestStorePayRequestRepository.findOneByOrderGuid(UUID.fromString(orderGuid))
                .orElse(new StorePayRequestNotificationEntity()));
    }
}
