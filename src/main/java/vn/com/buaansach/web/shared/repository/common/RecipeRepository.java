package vn.com.buaansach.web.shared.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.RecipeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Optional<RecipeEntity> findOneByGuid(UUID guid);

    void deleteByGuid(UUID recipeGuid);

    List<RecipeEntity> findAllByOrderByRecipePositionAsc();
}
