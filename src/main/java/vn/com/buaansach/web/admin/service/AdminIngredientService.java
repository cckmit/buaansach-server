package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.IngredientEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.common.AdminIngredientRepository;
import vn.com.buaansach.web.admin.repository.common.AdminProductIngredientRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AdminIngredientService {
    private final AdminIngredientRepository adminIngredientRepository;
    private final AdminProductIngredientRepository adminProductIngredientRepository;

    public IngredientEntity createIngredient(IngredientEntity payload, MultipartFile image){
        payload.setGuid(UUID.randomUUID());
        return adminIngredientRepository.save(payload);
    }

    public IngredientEntity updateIngredient(IngredientEntity payload, MultipartFile image){
        IngredientEntity entity = adminIngredientRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(()-> new NotFoundException(ErrorCode.INGREDIENT_NOT_FOUND));
        payload.setId(entity.getId());
        payload.setGuid(UUID.randomUUID());
        return adminIngredientRepository.save(payload);
    }

    public List<IngredientEntity> getIngredients(){
        return adminIngredientRepository.findAll();
    }

    @Transactional
    public void deleteIngredient(UUID ingredientGuid){
        adminIngredientRepository.deleteByGuid(ingredientGuid);
        adminProductIngredientRepository.deleteByIngredientGuid(ingredientGuid);
    }
}
