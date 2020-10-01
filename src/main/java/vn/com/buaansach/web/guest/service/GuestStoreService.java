package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.guest.repository.notification.GuestStoreNotificationRepository;
import vn.com.buaansach.web.guest.repository.store.GuestSeatRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreRepository;
import vn.com.buaansach.web.guest.security.GuestStoreSecurity;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreDTO;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreNotificationDTO;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.guest.service.dto.write.GuestCallWaiterDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreService {
    private final GuestStoreRepository guestStoreRepository;
    private final GuestSocketService guestSocketService;
    private final GuestSeatRepository guestSeatRepository;
    private final GuestStoreNotificationRepository guestStoreNotificationRepository;
    private final GuestStoreSecurity guestStoreSecurity;

    public GuestStoreDTO getStoreBySeat(String seatGuid) {
        return new GuestStoreDTO(guestStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND)));
    }

    public void callWaiter(GuestCallWaiterDTO payload) {
        guestStoreSecurity.blockAccessIfStoreIsNotOpenOrDeactivated(payload.getStoreGuid());

        guestStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        StoreNotificationEntity notificationEntity = new StoreNotificationEntity();
        notificationEntity.setGuid(UUID.randomUUID());
        notificationEntity.setStoreNotificationStatus(StoreNotificationStatus.UNSEEN);
        notificationEntity.setStoreNotificationType(StoreNotificationType.CALL_WAITER);
        notificationEntity.setStoreNotificationHidden(false);
        notificationEntity.setStoreGuid(payload.getStoreGuid());
        notificationEntity.setAreaGuid(seatEntity.getAreaGuid());
        notificationEntity.setSeatGuid(seatEntity.getGuid());
        guestStoreNotificationRepository.save(notificationEntity);

        guestSocketService.sendCallWaiterNotification(new GuestStoreNotificationDTO(guestStoreNotificationRepository.save(notificationEntity)));
    }
}
