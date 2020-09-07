package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.web.pos.repository.common.PosCategoryRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreProductRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosCategoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosCategoryService {
    private final PosCategoryRepository posCategoryRepository;
    private final PosStoreProductRepository posStoreProductRepository;

    public List<PosCategoryDTO> getListPosCategoryDTO(String storeGuid) {
        List<CategoryEntity> categories = posCategoryRepository.findAllCategoryOrderByPositionAsc();
        List<PosCategoryDTO> result = new ArrayList<>();
        categories.forEach(category -> {
            PosCategoryDTO dto = new PosCategoryDTO(category);
            dto.setListStoreProduct(posStoreProductRepository.findListPosStoreProductByStoreAndCategory(
                    UUID.fromString(storeGuid),
                    category.getGuid()));
            result.add(dto);
        });
        return result;
    }

}
