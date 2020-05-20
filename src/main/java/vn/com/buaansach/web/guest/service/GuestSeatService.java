package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestSeatRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestSeatService {
    private final GuestSeatRepository guestSeatRepository;

    public GuestSeatDTO getSeat(String seatGuid) {
        return guestSeatRepository.findGuestSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("guest@seatNotFound@" + seatGuid));
    }

    public void makeSeatServiceUnfinished(UUID seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new ResourceNotFoundException("guest@seatNotFound@" + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        guestSeatRepository.save(seatEntity);
    }

    public boolean isOrderMatchesSeat(String orderGuid, String seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@seatNotFound@" + seatGuid));
        if (seatEntity.getCurrentOrderGuid() == null && orderGuid == null) return true;
        if (seatEntity.getCurrentOrderGuid() == null && orderGuid != null) return false;
        if (seatEntity.getCurrentOrderGuid() != null && orderGuid == null) return false;
        if (seatEntity.getCurrentOrderGuid() != null && orderGuid != null) {
            return seatEntity.getCurrentOrderGuid().toString().equals(orderGuid);
        }
        return false;
    }
}
