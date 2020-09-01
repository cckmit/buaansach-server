package vn.com.buaansach.web.guest.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuestProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByGuidIn(List<UUID> uuids);

    @Query("SELECT product FROM ProductEntity product " +
            "JOIN vn.com.buaansach.entity.common.ProductCategoryEntity productCategory " +
            "ON productCategory.productGuid = product.guid " +
            "WHERE productCategory.categoryGuid = :categoryGuid " +
            "AND product.productStatus <> :productStatus " +
            "ORDER BY product.productPosition ASC")
    List<ProductEntity> findByCategoryGuidExceptStatus(@Param("categoryGuid") UUID categoryGuid, @Param("productStatus") ProductStatus productStatus);
}
