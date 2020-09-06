package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.shared.service.PaymentService;
import vn.com.buaansach.web.guest.repository.notification.GuestStoreNotificationRepository;
import vn.com.buaansach.web.guest.repository.notification.GuestStorePayRequestNotificationRepository;
import vn.com.buaansach.web.guest.repository.order.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.store.GuestSeatRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreNotificationDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestStorePayRequestDTO;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStorePayRequestNotificationService {
    private final GuestStorePayRequestNotificationRepository guestStorePayRequestNotificationRepository;
    private final GuestOrderRepository guestOrderRepository;
    private final GuestStoreRepository guestStoreRepository;
    private final GuestSeatRepository guestSeatRepository;
    private final GuestSocketService guestSocketService;
    private final PaymentService paymentService;
    private final GuestStoreNotificationRepository guestStoreNotificationRepository;

    @Transactional
    public GuestStorePayRequestDTO sendRequest(GuestStorePayRequestDTO payload) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        SeatEntity seatEntity = guestSeatRepository.findOneByOrderGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(seatEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));


        if (guestStorePayRequestNotificationRepository.findOneByOrderGuid(orderEntity.getGuid()).isPresent())
            throw new BadRequestException(ErrorCode.STORE_PAY_REQUEST_EXIST);

        long payAmount = paymentService.calculatePayAmount(orderEntity);
        if (payAmount > payload.getStorePayRequestAmount())
            throw new BadRequestException(ErrorCode.PAY_AMOUNT_NOT_ENOUGH);

        if (!seatEntity.getOrderGuid().equals(orderEntity.getGuid()))
            throw new BadRequestException(ErrorCode.ORDER_AND_SEAT_NOT_MATCH);

        StoreNotificationEntity notificationEntity = new StoreNotificationEntity();
        UUID notificationGuid = UUID.randomUUID();
        notificationEntity.setGuid(notificationGuid);
        notificationEntity.setStoreNotificationStatus(StoreNotificationStatus.UNSEEN);
        notificationEntity.setStoreNotificationType(StoreNotificationType.PAY_REQUEST);
        notificationEntity.setStoreNotificationHidden(false);
        notificationEntity.setStoreGuid(storeEntity.getGuid());
        notificationEntity.setAreaGuid(seatEntity.getAreaGuid());
        notificationEntity.setSeatGuid(seatEntity.getGuid());
        notificationEntity = guestStoreNotificationRepository.save(notificationEntity);

        StorePayRequestNotificationEntity payRequestNotification = new StorePayRequestNotificationEntity();
        payRequestNotification.setStorePayRequestAmount(payload.getStorePayRequestAmount());
        payRequestNotification.setStorePayRequestNote(payload.getStorePayRequestNote());
        payRequestNotification.setNumberOfExtraSeat(payload.getNumberOfExtraSeat());
        payRequestNotification.setListExtraSeat(payload.getListExtraSeat());
        payRequestNotification.setListExtraOrder(payload.getListExtraOrder());
        payRequestNotification.setOrderGuid(orderEntity.getGuid());
        payRequestNotification.setStoreNotificationGuid(notificationGuid);
        payRequestNotification = guestStorePayRequestNotificationRepository.save(payRequestNotification);

        guestSocketService.sendPayRequestNotification(new GuestStoreNotificationDTO(notificationEntity, payRequestNotification));
        return new GuestStorePayRequestDTO(payRequestNotification);
    }

    public GuestStorePayRequestDTO getByOrderGuid(String orderGuid) {
        return new GuestStorePayRequestDTO(guestStorePayRequestNotificationRepository.findOneByOrderGuid(UUID.fromString(orderGuid))
                .orElse(new StorePayRequestNotificationEntity()));
    }
}