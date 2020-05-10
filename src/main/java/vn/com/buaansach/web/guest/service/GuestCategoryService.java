package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.guest.repository.GuestCategoryRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestCategoryDTO;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GuestCategoryService {
    private final GuestCategoryRepository guestCategoryRepository;
    private final GuestStoreProductRepository guestStoreProductRepository;

    public GuestCategoryService(GuestCategoryRepository guestCategoryRepository, GuestStoreProductRepository guestStoreProductRepository) {
        this.guestCategoryRepository = guestCategoryRepository;
        this.guestStoreProductRepository = guestStoreProductRepository;
    }

    public List<GuestCategoryDTO> getListGuestCategoryDTO(String storeGuid) {
        List<CategoryEntity> categories = guestCategoryRepository.findAllCategoryOrderByPosition();
        List<GuestCategoryDTO> result = new ArrayList<>();
        categories.forEach(category -> {
            GuestCategoryDTO dto = new GuestCategoryDTO(category);
            List<GuestStoreProductDTO> storeProductDTOS = guestStoreProductRepository
                    .findListGuestStoreProductByStoreAndCategoryExceptStatus(UUID.fromString(storeGuid), category.getGuid(), ProductStatus.STOP_TRADING);
            dto.setListStoreProduct(storeProductDTOS);
            result.add(dto);
        });
        return result;
    }
}
