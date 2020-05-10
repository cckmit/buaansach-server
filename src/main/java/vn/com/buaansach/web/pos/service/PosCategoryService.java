package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.web.pos.repository.PosCategoryRepository;
import vn.com.buaansach.web.pos.repository.PosStoreProductRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosCategoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PosCategoryService {
    private final PosCategoryRepository posCategoryRepository;
    private final PosStoreProductRepository posStoreProductRepository;

    public PosCategoryService(PosCategoryRepository posCategoryRepository, PosStoreProductRepository posStoreProductRepository) {
        this.posCategoryRepository = posCategoryRepository;
        this.posStoreProductRepository = posStoreProductRepository;
    }

    public List<PosCategoryDTO> getListPosCategoryDTO(String storeGuid) {
        List<CategoryEntity> categories = posCategoryRepository.findAllCategoryOrderByPosition();
        List<PosCategoryDTO> result = new ArrayList<>();
        categories.forEach(category -> {
            PosCategoryDTO dto = new PosCategoryDTO(category);
            dto.setListStoreProduct(posStoreProductRepository.findListPosStoreProductByStoreAndCategory(UUID.fromString(storeGuid), category.getGuid()));
            result.add(dto);
        });
        return result;
    }

}
