package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.web.pos.repository.PosCategoryRepository;

import java.util.List;

@Service
public class PosCategoryService {
    private final PosCategoryRepository posCategoryRepository;

    public PosCategoryService(PosCategoryRepository posCategoryRepository) {
        this.posCategoryRepository = posCategoryRepository;
    }

    public List<CategoryEntity> getListPosCategoryDTO() {
        List<CategoryEntity> result = posCategoryRepository.findAll();
        return result;
    }
}
