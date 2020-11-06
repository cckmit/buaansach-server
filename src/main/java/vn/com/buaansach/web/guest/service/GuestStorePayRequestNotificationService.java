package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.entity.notification.StoreNotificationEntity;
import vn.com.buaansach.entity.notification.StorePayRequestNotificationEntity;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.guest.repository.notification.GuestStoreNotificationRepository;
import vn.com.buaansach.web.guest.repository.notification.GuestStorePayRequestNotificationRepository;
import vn.com.buaansach.web.guest.repository.order.GuestOrderProductRepository;
import vn.com.buaansach.web.guest.repository.order.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.store.GuestSeatRepository;
import vn.com.buaansach.web.guest.repository.store.GuestStoreRepository;
import vn.com.buaansach.web.guest.security.GuestStoreSecurity;
import vn.com.buaansach.web.guest.service.dto.write.GuestStorePayRequestDTO;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.shared.service.PriceService;
import vn.com.buaansach.web.shared.service.dto.readwrite.StoreNotificationDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStorePayRequestNotificationService {
    private final GuestStorePayRequestNotificationRepository guestStorePayRequestNotificationRepository;
    private final GuestOrderRepository guestOrderRepository;
    private final GuestStoreRepository guestStoreRepository;
    private final GuestSeatRepository guestSeatRepository;
    private final GuestSocketService guestSocketService;
    private final GuestStoreSecurity guestStoreSecurity;
    private final PriceService priceService;
    private final GuestStoreNotificationRepository guestStoreNotificationRepository;
    private final GuestOrderProductRepository guestOrderProductRepository;

    @Transactional
    public GuestStorePayRequestDTO sendRequest(GuestStorePayRequestDTO payload) {
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        SeatEntity seatEntity = guestSeatRepository.findOneByOrderGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(seatEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        guestStoreSecurity.blockAccessIfStoreAbnormal(storeEntity.getGuid());

        if (seatEntity.isSeatLocked())
            throw new BadRequestException(ErrorCode.SEAT_LOCKED);

        List<StoreNotificationEntity> list = guestStoreNotificationRepository
                .findByOrderGuidAndStoreNotificationType(orderEntity.getGuid(), StoreNotificationType.PAY_REQUEST);
        if (!list.isEmpty()) throw new BadRequestException(ErrorCode.STORE_PAY_REQUEST_EXIST);

        List<OrderProductStatus> listStatus = new ArrayList<>();
        listStatus.add(OrderProductStatus.CREATED);
        listStatus.add(OrderProductStatus.PREPARING);
        List<OrderProductEntity> listOrderProduct = guestOrderProductRepository.findByOrderGuidAndOrderProductStatusIn(orderEntity.getGuid(), listStatus);
        if (listOrderProduct.size() != 0) {
            throw new BadRequestException(ErrorCode.ORDER_UNFINISHED);
        }

        long payAmount = priceService.calculatePayAmount(orderEntity);
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
        notificationEntity.setOrderGuid(orderEntity.getGuid());
        notificationEntity = guestStoreNotificationRepository.save(notificationEntity);

        StorePayRequestNotificationEntity payRequestNotification = new StorePayRequestNotificationEntity();
        payRequestNotification.setStorePayRequestAmount(payload.getStorePayRequestAmount());
        payRequestNotification.setStorePayRequestMethod(payload.getStorePayRequestMethod());
        payRequestNotification.setStorePayRequestNote(payload.getStorePayRequestNote());
        payRequestNotification.setKeepTheChange(payload.isKeepTheChange());
        payRequestNotification.setNumberOfExtraSeat(payload.getNumberOfExtraSeat());
        payRequestNotification.setListExtraSeat(payload.getListExtraSeat());
        payRequestNotification.setListExtraOrder(payload.getListExtraOrder());
        payRequestNotification.setStoreNotificationGuid(notificationGuid);
        payRequestNotification = guestStorePayRequestNotificationRepository.save(payRequestNotification);

        guestSocketService.sendPayRequestNotification(new StoreNotificationDTO(notificationEntity, payRequestNotification));
        return new GuestStorePayRequestDTO(notificationEntity, payRequestNotification);
    }

    public GuestStorePayRequestDTO getByOrderGuid(String orderGuid) {
        List<StoreNotificationEntity> list = guestStoreNotificationRepository
                .findByOrderGuidAndStoreNotificationType(UUID.fromString(orderGuid), StoreNotificationType.PAY_REQUEST);
        StoreNotificationEntity storeNotificationEntity = new StoreNotificationEntity();
        if (list.size() != 0) storeNotificationEntity = list.get(0);
        StorePayRequestNotificationEntity payRequestNotificationEntity = guestStorePayRequestNotificationRepository
                .findOneByStoreNotificationGuid(storeNotificationEntity.getGuid())
                .orElse(new StorePayRequestNotificationEntity());
        return new GuestStorePayRequestDTO(storeNotificationEntity, payRequestNotificationEntity);
    }
}
