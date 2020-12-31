package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.entity.common.RecipeEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.common.AdminRecipeRepository;
import vn.com.buaansach.web.shared.service.FileService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AdminRecipeService {
    private final AdminRecipeRepository adminRecipeRepository;
    private final FileService fileService;

    public List<RecipeEntity> getRecipes(){
        return adminRecipeRepository.findAllByOrderByRecipePositionAsc();
    }

    public RecipeEntity createRecipe(RecipeEntity payload, MultipartFile image){
        payload.setGuid(UUID.randomUUID());
        if (image != null){
            FileEntity file = fileService.uploadImage(image, Constants.RECIPE_IMAGE_PATH);
            payload.setRecipeImageUrl(file.getUrl());
        }
        return adminRecipeRepository.save(payload);
    }

    public RecipeEntity updateRecipe(RecipeEntity payload, MultipartFile image){
        RecipeEntity entity = adminRecipeRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(()-> new NotFoundException(ErrorCode.RECIPE_NOT_FOUND));
        payload.setId(entity.getId());
        if (image != null){
            fileService.deleteByUrl(entity.getRecipeImageUrl());
            FileEntity file = fileService.uploadImage(image, Constants.RECIPE_IMAGE_PATH);
            payload.setRecipeImageUrl(file.getUrl());
        }
        return adminRecipeRepository.save(payload);
    }

    @Transactional
    public void deleteRecipe(UUID recipeGuid){
        adminRecipeRepository.deleteByGuid(recipeGuid);
    }
}
