package vn.com.buaansach.service.guest;

import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.SeatRepository;
import vn.com.buaansach.service.dto.guest.GuestSeatDTO;

import java.util.UUID;

@Service
public class GuestOrderService {
    private final SeatRepository seatRepository;

    public GuestOrderService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public GuestSeatDTO getSeat(String seatGuid) {
        return seatRepository.findGuestSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
    }
}
