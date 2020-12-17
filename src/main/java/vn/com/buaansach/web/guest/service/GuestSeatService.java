package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.guest.repository.order.GuestOrderRepository;
import vn.com.buaansach.web.guest.repository.store.GuestSeatRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestCheckOrderSeatDTO;
import vn.com.buaansach.web.shared.service.SeatIdentityService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GuestSeatService {
    private final GuestSeatRepository guestSeatRepository;
    private final GuestOrderRepository guestOrderRepository;
    private final SeatIdentityService seatIdentityService;

    public GuestSeatDTO getSeat(String seatGuid) {
        return guestSeatRepository.findGuestSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
    }

    public void makeSeatServiceUnfinished(UUID seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        guestSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceUnfinished(SeatEntity seatEntity) {
        seatEntity.setSeatStatus(SeatStatus.NON_EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        guestSeatRepository.save(seatEntity);
    }

    public GuestCheckOrderSeatDTO checkOrderSeat(GuestCheckOrderSeatDTO payload) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(payload.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

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

        /* Nếu trên máy không có mã đơn hợp lệ được lưu và chỗ đang có đơn => kiểm tra thông tin máy */
        if (!payload.isHasValidOrderGuid() && seatEntity.getOrderGuid() != null){
            /* Nếu thông tin máy hợp lệ thì thêm mã đơn vào danh sách lưu lại trên máy khách */
            if (seatIdentityService.isSeatIdentityMatched(payload.getSeatIdentity(), payload.getSeatGuid())){
                listValidOrderGuid.add(seatEntity.getOrderGuid());
                payload.setHasValidOrderGuid(true);
                payload.setActiveOrderGuid(seatEntity.getOrderGuid());
            }
        }

        payload.setListOrderGuid(listValidOrderGuid);

        return payload;
    }
}
