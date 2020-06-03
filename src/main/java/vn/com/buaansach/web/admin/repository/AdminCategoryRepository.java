package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;

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

    @Query("SELECT ce FROM CategoryEntity ce ORDER BY ce.categoryPosition ASC")
    List<CategoryEntity> findListCategory();

    @Query("SELECT MAX(ce.categoryPosition) FROM CategoryEntity ce")
    Integer findLastCategoryPosition();

    @Modifying
    @Query(value = "UPDATE bas_category SET category_position = ?2  WHERE guid = ?1", nativeQuery = true)
    void updatePosition(UUID categoryGuid, int pos);
}
