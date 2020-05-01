package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findOneByCategoryName(String categoryName);

    Optional<CategoryEntity> findOneByGuid(UUID guid);

    @Query(value = "SELECT bc.* FROM bas_category bc WHERE bc.guid IN " +
            "(SELECT bpc.category_guid FROM bas_product_category bpc " +
            "WHERE bpc.product_guid = :productGuid)", nativeQuery = true)
    List<CategoryEntity> findListCategoryByProductGuid(@Param("productGuid") UUID productGuid);


    @Query(value = "SELECT bc.* FROM bas_category bc WHERE bc.guid IN " +
            "(SELECT bpc.category_guid FROM bas_product_category bpc " +
            "WHERE bpc.category_guid = :categoryGuid)", nativeQuery = true)
    List<ProductEntity> findListProductByProductGuid(@Param("categoryGuid") UUID categoryGuid);
}
