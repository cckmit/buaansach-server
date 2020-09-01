package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.guest.repository.store.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreProductService {
    private final GuestStoreProductRepository guestStoreProductRepository;

    public List<GuestStoreProductDTO> getListStoreProduct(String storeGuid) {
        return guestStoreProductRepository.findListGuestStoreProductDTOExceptStatus(UUID.fromString(storeGuid), ProductStatus.STOP_TRADING);
    }
}
