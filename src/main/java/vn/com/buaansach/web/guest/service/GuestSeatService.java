package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
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
}
