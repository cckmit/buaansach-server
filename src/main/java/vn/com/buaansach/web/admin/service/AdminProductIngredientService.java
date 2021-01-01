package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductIngredientEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.common.AdminProductIngredientRepository;
import vn.com.buaansach.web.admin.repository.common.AdminProductRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminProductIngredientDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AdminProductIngredientService {
    private final AdminProductRepository adminProductRepository;
    private final AdminProductIngredientRepository adminProductIngredientRepository;

    @Transactional
    public void createProductIngredient(AdminProductIngredientDTO payload){
        adminProductRepository.findOneByGuid(payload.getProductGuid())
                .orElseThrow(()-> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        adminProductIngredientRepository.deleteByProductGuid(payload.getProductGuid());

        List<ProductIngredientEntity> list = new ArrayList<>();
        for (int i = 0; i < payload.getListProductIngredient().size(); i++){
            ProductIngredientEntity entity = new ProductIngredientEntity();
            entity.setProductGuid(payload.getProductGuid());
            entity.setIngredientGuid(payload.getListProductIngredient().get(i).getIngredientGuid());
            entity.setProductIngredientAmount(payload.getListProductIngredient().get(i).getProductIngredientAmount());
            list.add(entity);
        }
        adminProductIngredientRepository.saveAll(list);
    }

    public List<ProductIngredientEntity> getProductIngredientByProductGuid(UUID productGuid){
        return adminProductIngredientRepository.findByProductGuid(productGuid);
    }
}
