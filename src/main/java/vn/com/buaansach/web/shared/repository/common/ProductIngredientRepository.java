package vn.com.buaansach.web.shared.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.ProductCategoryEntity;
import vn.com.buaansach.entity.common.ProductIngredientEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductIngredientRepository extends JpaRepository<ProductIngredientEntity, Long> {
    void deleteByProductGuid(UUID productGuid);

    void deleteByIngredientGuid(UUID categoryGuid);

    List<ProductIngredientEntity> findByProductGuid(UUID productGuid);

    List<ProductIngredientEntity> findByIngredientGuid(UUID ingredientGuid);
}
