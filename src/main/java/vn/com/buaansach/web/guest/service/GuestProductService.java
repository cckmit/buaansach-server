package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.web.guest.repository.GuestProductRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestProductDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestProductService {
    private final GuestProductRepository guestProductRepository;

    public List<GuestProductDTO> getAllProduct() {
        return guestProductRepository.findAll().stream().map(GuestProductDTO::new).collect(Collectors.toList());
    }
}
