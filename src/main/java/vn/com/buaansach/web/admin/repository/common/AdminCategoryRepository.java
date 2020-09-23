package vn.com.buaansach.web.admin.repository.common;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.common.CategoryRepository;
import vn.com.buaansach.entity.common.CategoryEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminCategoryRepository extends CategoryRepository {
    @Query(value = "SELECT bc.* FROM bas_category bc WHERE bc.guid IN " +
            "(SELECT bpc.category_guid FROM bas_product_category bpc " +
            "WHERE bpc.product_guid = :productGuid)", nativeQuery = true)
    List<CategoryEntity> findListCategoryByProductGuid(@Param("productGuid") UUID productGuid);

    @Query("SELECT MAX(ce.categoryPosition) FROM CategoryEntity ce")
    Integer findLastCategoryPosition();

    @Modifying
    @Query(value = "UPDATE bas_category SET category_position = :pos  WHERE guid = :categoryGuid", nativeQuery = true)
    void updatePosition(@Param("categoryGuid") UUID categoryGuid, @Param("pos") int pos);
}
