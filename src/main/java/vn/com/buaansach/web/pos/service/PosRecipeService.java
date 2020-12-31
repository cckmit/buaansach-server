package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.RecipeEntity;
import vn.com.buaansach.web.pos.repository.common.PosRecipeRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PosRecipeService {
    private final PosRecipeRepository posRecipeRepository;

    public List<RecipeEntity> getRecipes() {
        return posRecipeRepository.findAllByOrderByRecipePositionAsc();
    }
}
