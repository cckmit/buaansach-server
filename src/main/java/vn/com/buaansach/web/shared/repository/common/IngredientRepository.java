package vn.com.buaansach.web.shared.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.IngredientEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    Optional<IngredientEntity> findOneByGuid(UUID guid);

    void deleteByGuid(UUID ingredientGuid);

    List<IngredientEntity> findAllByOrderByIngredientPosition();
}
