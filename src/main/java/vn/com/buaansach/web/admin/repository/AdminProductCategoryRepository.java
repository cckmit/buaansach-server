package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.ProductCategoryEntity;
import vn.com.buaansach.entity.common.ProductEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

    @Query(value = "SELECT bc.* FROM bas_product_category bpc " +
            "LEFT JOIN bas_product bp ON bpc.product_guid = bp.guid " +
            "LEFT JOIN bas_category bc ON bpc.category_guid = bc.guid " +
            "WHERE bpc.product_guid = :productGuid", nativeQuery = true)
    List<CategoryEntity> findListCategoryByProductGuid(@Param("productGuid") UUID productGuid);

    @Query(value = "SELECT bc.* FROM bas_product_category bpc " +
            "LEFT JOIN bas_product bp ON bpc.product_guid = bp.guid " +
            "LEFT JOIN bas_category bc ON bpc.category_guid = bc.guid " +
            "WHERE bpc.category_guid = :categoryGuid", nativeQuery = true)
    List<ProductEntity> findListProductByProductGuid(@Param("categoryGuid") UUID categoryGuid);

    void deleteByProductGuid(UUID productGuid);

    void deleteByCategoryGuid(UUID categoryGuid);
}
