package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.SeatRepository;
import vn.com.buaansach.web.customer.service.dto.CustomerSeatDTO;

import java.util.UUID;

@Service
public class CustomerSeatService {
    private final SeatRepository seatRepository;

    public CustomerSeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public CustomerSeatDTO getSeat(String seatGuid) {
        return seatRepository.findGuestSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chỗ ngồi với id: " + seatGuid));
    }
}
