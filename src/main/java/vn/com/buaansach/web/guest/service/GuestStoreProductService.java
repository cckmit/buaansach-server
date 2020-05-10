package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.guest.repository.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Service
public class GuestStoreProductService {
    private final GuestStoreProductRepository guestStoreProductRepository;

    public GuestStoreProductService(GuestStoreProductRepository guestStoreProductRepository) {
        this.guestStoreProductRepository = guestStoreProductRepository;
    }

    public List<GuestStoreProductDTO> getListStoreProduct(String storeGuid) {
        return guestStoreProductRepository.findListGuestStoreProductDTOExceptStatus(UUID.fromString(storeGuid), ProductStatus.STOP_TRADING);
    }
}
