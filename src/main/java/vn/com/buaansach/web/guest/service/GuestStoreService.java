package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreService {
    private final GuestStoreRepository guestStoreRepository;

    public GuestStoreDTO getStoreBySeat(String seatGuid) {
        return new GuestStoreDTO(guestStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("storeNotFound")));
    }
}