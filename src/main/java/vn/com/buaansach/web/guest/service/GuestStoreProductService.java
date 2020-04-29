package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.web.guest.repository.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.service.dto.GuestProductDTO;

import java.util.List;
import java.util.UUID;

@Service
public class GuestStoreProductService {
    private final GuestStoreProductRepository guestStoreProductRepository;

    public GuestStoreProductService(GuestStoreProductRepository guestStoreProductRepository) {
        this.guestStoreProductRepository = guestStoreProductRepository;
    }

    public List<GuestProductDTO> getListStoreProduct(String storeGuid) {
        return guestStoreProductRepository.findListGuestStoreProductDTO(UUID.fromString(storeGuid));
    }
}
