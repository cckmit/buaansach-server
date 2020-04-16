package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.customer.repository.CustomerSeatRepository;
import vn.com.buaansach.web.customer.service.dto.CustomerSeatDTO;

import java.util.UUID;

@Service
public class CustomerSeatService {
    private final CustomerSeatRepository customerSeatRepository;

    public CustomerSeatService(CustomerSeatRepository customerSeatRepository) {
        this.customerSeatRepository = customerSeatRepository;
    }

    public CustomerSeatDTO getSeat(String seatGuid) {
        return customerSeatRepository.findCustomerSeatDTO(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
    }
}
