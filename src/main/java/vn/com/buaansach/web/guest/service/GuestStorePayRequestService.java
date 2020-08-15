package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StorePayRequestStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StorePayRequestEntity;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.guest.exception.GuestBadRequestException;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.*;
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
    private final GuestOrderService guestOrderService;

    @Transactional
    public GuestStorePayRequestDTO sendRequest(GuestStorePayRequestDTO payload) {
        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(payload.getSeatGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@storeNotFoundWithSeat@" + payload.getSeatGuid()));
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@seatNotFound@" + payload.getSeatGuid()));
        OrderEntity orderEntity = guestOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@orderNotFound@" + payload.getOrderGuid()));

        if (guestStorePayRequestRepository.findOneByOrderGuid(orderEntity.getGuid()).isPresent())
            throw new GuestBadRequestException("guest@storePayRequestExistWithOrderGuid@" + orderEntity.getGuid());

        long payAmount = posPaymentService.calculatePayAmount(orderEntity);
        if (payAmount > payload.getPayAmount())
            throw new GuestBadRequestException("guest@payAmountNotEnough@" + payAmount);

        if (!seatEntity.getCurrentOrderGuid().equals(orderEntity.getGuid()))
            throw new GuestBadRequestException("guest@orderNotMatchSeat@orderGuid=" + orderEntity.getGuid() + ";seatOrderGuid=" + seatEntity.getCurrentOrderGuid());

        /* Cập nhật SDT cho khách nếu có và đơn phải chưa có SĐT */
        if (payload.getCustomerPhone() != null){
            if (orderEntity.getCustomerPhone() == null){
                guestOrderService.updateCustomerPhone(payload.getOrderGuid(), payload.getCustomerPhone());
            } else {
                throw new GuestBadRequestException("guest@orderCustomerPhoneExist@" + orderEntity.getGuid());
            }
        }

        StorePayRequestEntity storePayRequestEntity = new StorePayRequestEntity();
        storePayRequestEntity.setGuid(UUID.randomUUID());
        storePayRequestEntity.setAreaGuid(seatEntity.getAreaGuid());
        storePayRequestEntity.setStoreGuid(storeEntity.getGuid());
        storePayRequestEntity.setPayAmount(payload.getPayAmount());
        storePayRequestEntity.setPayNote(payload.getPayNote());
        storePayRequestEntity.setStorePayRequestStatus(StorePayRequestStatus.UNSEEN);
        storePayRequestEntity.setSeatGuid(seatEntity.getGuid());
        storePayRequestEntity.setOrderGuid(orderEntity.getGuid());
        StorePayRequestEntity result = guestStorePayRequestRepository.save(storePayRequestEntity);

        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketConstants.GUEST_STORE_PAY_REQUEST, storePayRequestEntity);
        guestSocketService.sendMessage(WebSocketConstants.TOPIC_POS_PREFIX + storeEntity.getGuid(), socketDTO);
        return new GuestStorePayRequestDTO(result);
    }

    public GuestStorePayRequestDTO getByOrderGuid(String orderGuid) {
        return new GuestStorePayRequestDTO(guestStorePayRequestRepository.findOneByOrderGuid(UUID.fromString(orderGuid))
                .orElse(new StorePayRequestEntity()));
    }
}
