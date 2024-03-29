package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.web.pos.repository.common.PosCategoryRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreProductRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosCategoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosCategoryService {
    private final PosCategoryRepository posCategoryRepository;
    private final PosStoreProductRepository posStoreProductRepository;
    private final PosStoreSecurity posStoreSecurity;

    public List<PosCategoryDTO> getListPosCategoryDTO(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        List<CategoryEntity> categories = posCategoryRepository.findAllByOrderByCategoryPositionAsc();
        List<PosCategoryDTO> result = new ArrayList<>();
        categories.forEach(category -> {
            PosCategoryDTO dto = new PosCategoryDTO(category);
            dto.setListStoreProduct(posStoreProductRepository.findListActivePosStoreProductByStoreAndCategoryExceptStatus(
                    UUID.fromString(storeGuid),
                    category.getGuid(),
                    StoreProductStatus.STOP_TRADING));
            result.add(dto);
        });
        return result;
    }

}
