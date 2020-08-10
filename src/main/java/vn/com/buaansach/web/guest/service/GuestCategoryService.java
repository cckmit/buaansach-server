package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestCategoryRepository;
import vn.com.buaansach.web.guest.repository.GuestProductRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreProductRepository;
import vn.com.buaansach.web.guest.repository.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestCategoryDTO;
import vn.com.buaansach.web.guest.service.dto.read.GuestProductDTO;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreCategoryDTO;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestCategoryService {
    private final GuestCategoryRepository guestCategoryRepository;
    private final GuestStoreProductRepository guestStoreProductRepository;
    private final GuestStoreRepository guestStoreRepository;
    private final GuestProductRepository guestProductRepository;

    public List<GuestStoreCategoryDTO> getListGuestStoreCategoryDTO(String seatGuid) {
        StoreEntity storeEntity = guestStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@storeNotFoundWithSeat@" + seatGuid));

        List<CategoryEntity> categories = guestCategoryRepository.findAllCategoryOrderByPositionAsc();
        List<GuestStoreCategoryDTO> result = new ArrayList<>();
        categories.forEach(category -> {
            GuestStoreCategoryDTO dto = new GuestStoreCategoryDTO(category);
            List<GuestStoreProductDTO> storeProductDTOS = guestStoreProductRepository
                    .findListGuestStoreProductByStoreAndCategoryExceptStatus(storeEntity.getGuid(), category.getGuid(), StoreProductStatus.STOP_TRADING);
            dto.setListStoreProduct(storeProductDTOS);
            result.add(dto);
        });
        return result;
    }

    public List<GuestCategoryDTO> getListCategoryDTO() {
        List<CategoryEntity> categories = guestCategoryRepository.findAllCategoryOrderByPositionAsc();
        List<GuestCategoryDTO> result = new ArrayList<>();
        categories.forEach(category -> {
            GuestCategoryDTO dto = new GuestCategoryDTO(category);
            List<ProductEntity> products = guestProductRepository.findByCategoryGuidExceptStatus(category.getGuid(), ProductStatus.STOP_TRADING);
            dto.setListProduct(products.stream().map(GuestProductDTO::new).collect(Collectors.toList()));
            result.add(dto);
        });
        return result;
    }
}
