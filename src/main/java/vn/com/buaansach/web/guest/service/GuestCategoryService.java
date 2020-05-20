package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestCategoryRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestCategoryDTO;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestCategoryService {
    private final GuestCategoryRepository guestCategoryRepository;
    private final GuestStoreProductRepository guestStoreProductRepository;
    private final GuestStoreRepository guestStoreRepository;

    public List<GuestCategoryDTO> getListGuestCategoryDTO(String seatGuid) {
        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@storeNotFoundWithSeat@" + seatGuid));

        List<CategoryEntity> categories = guestCategoryRepository.findAllCategoryOrderByPositionAsc();
        List<GuestCategoryDTO> result = new ArrayList<>();
        categories.forEach(category -> {
            GuestCategoryDTO dto = new GuestCategoryDTO(category);
            List<GuestStoreProductDTO> storeProductDTOS = guestStoreProductRepository
                    .findListGuestStoreProductByStoreAndCategoryExceptStatus(storeEntity.getGuid(), category.getGuid(), StoreProductStatus.STOP_TRADING);
            dto.setListStoreProduct(storeProductDTOS);
            result.add(dto);
        });
        return result;
    }
}
