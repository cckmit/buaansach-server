package vn.com.buaansach.web.guest.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.common.ProductRepository;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuestProductRepository extends ProductRepository {

    @Query("SELECT product FROM ProductEntity product " +
            "JOIN vn.com.buaansach.entity.common.ProductCategoryEntity productCategory " +
            "ON productCategory.productGuid = product.guid " +
            "WHERE productCategory.categoryGuid = :categoryGuid " +
            "AND product.productActivated = TRUE " +
            "AND product.productStatus <> :productStatus " +
            "ORDER BY product.productPosition ASC")
    List<ProductEntity> findActiveProductByCategoryGuidExceptStatus(@Param("categoryGuid") UUID categoryGuid, @Param("productStatus") ProductStatus productStatus);
}
