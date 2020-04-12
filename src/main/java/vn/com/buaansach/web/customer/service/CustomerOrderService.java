package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.SeatRepository;
import vn.com.buaansach.web.customer.service.dto.GuestSeatDTO;

import java.util.UUID;

@Service
public class CustomerOrderService {
    private final SeatRepository seatRepository;

    public CustomerOrderService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public GuestSeatDTO getSeat(String seatGuid) {
        return seatRepository.findGuestSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
    }
}
