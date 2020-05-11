package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestSeatRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;

import java.util.UUID;

@Service
public class GuestSeatService {
    private final GuestSeatRepository guestSeatRepository;

    public GuestSeatService(GuestSeatRepository guestSeatRepository) {
        this.guestSeatRepository = guestSeatRepository;
    }

    public GuestSeatDTO getSeat(String seatGuid) {
        return guestSeatRepository.findGuestSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
    }

    public void resetSeat(SeatEntity seatEntity) {
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setCurrentOrderGuid(null);
        guestSeatRepository.save(seatEntity);
    }

    public void resetSeat(UUID seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setCurrentOrderGuid(null);
        guestSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceFinished(UUID seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        guestSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceUnfinished(UUID seatGuid) {
        SeatEntity seatEntity = guestSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        guestSeatRepository.save(seatEntity);
    }

}
