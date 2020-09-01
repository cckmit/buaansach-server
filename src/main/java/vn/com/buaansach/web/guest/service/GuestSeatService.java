package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.guest.repository.order.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.store.GuestSeatRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestCheckOrderSeatDTO;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GuestSeatService {
    private final GuestSeatRepository guestSeatRepository;
    private final GuestOrderRepository guestOrderRepository;

    public GuestSeatDTO getSeat(String seatGuid) {
        return guestSeatRepository.findGuestSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException("guest@seatNotFound@" + seatGuid));
    }

    public void makeSeatServiceUnfinished(UUID seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException("guest@seatNotFound@" + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        guestSeatRepository.save(seatEntity);
    }

    public boolean isOrderMatchesSeat(String orderGuid, String seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException("guest@seatNotFound@" + seatGuid));
        if (seatEntity.getOrderGuid() == null && orderGuid == null) return true;
        if (seatEntity.getOrderGuid() == null && orderGuid != null) return false;
        if (seatEntity.getOrderGuid() != null && orderGuid == null) return false;
        if (seatEntity.getOrderGuid() != null && orderGuid != null) {
            return seatEntity.getOrderGuid().toString().equals(orderGuid);
        }
        return false;
    }

    public GuestCheckOrderSeatDTO checkOrderSeat(GuestCheckOrderSeatDTO payload) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException("guest@seatNotFound@" + payload.getSeatGuid()));

        payload.setHasValidOrderGuid(false);
        payload.setActiveOrderGuid(null);

        Map<UUID, OrderStatus> mapOrder = new HashMap<>();
        guestOrderRepository.findByGuidIn(payload.getListOrderGuid()).forEach(item -> {
            mapOrder.put(item.getGuid(), item.getOrderStatus());
        });

        /* Gán lại list mã đơn hợp lệ trên máy khách */
        List<UUID> listValidOrderGuid = new ArrayList<>();
        payload.getListOrderGuid().forEach(orderGuid -> {
            OrderStatus status = mapOrder.get(orderGuid);
            if (OrderStatus.CREATED.equals(status) || OrderStatus.RECEIVED.equals(status)) {
                listValidOrderGuid.add(orderGuid);

                /* Tìm xem có mã đơn hợp lệ trong đống được lưu trên máy khách hay không */
                if (orderGuid.equals(seatEntity.getOrderGuid())) {
                    payload.setHasValidOrderGuid(true);
                    payload.setActiveOrderGuid(orderGuid);
                }
            }
        });
        payload.setListOrderGuid(listValidOrderGuid);
        return payload;
    }
}
