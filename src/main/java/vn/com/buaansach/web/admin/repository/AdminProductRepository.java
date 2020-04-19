package vn.com.buaansach.web.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findOneByGuid(UUID productGuid);

    Optional<ProductEntity> findOneByProductCode(String productCode);

    @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:search% OR p.productCode LIKE %:search%")
    Page<ProductEntity> findPageProductWithKeyword(Pageable pageable, @Param("search") String search);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.categoryGuid = NULL WHERE p.categoryGuid = :categoryGuid")
    void clearProductCategory(@Param("categoryGuid") UUID categoryGuid);

    @Query("SELECT product FROM ProductEntity product " +
            "WHERE product.productStatus <> 'STOP_TRADING' " +
            "AND product.guid NOT IN (" +
            "SELECT DISTINCT storeProduct.productGuid " +
            "FROM vn.com.buaansach.entity.StoreProductEntity storeProduct " +
            "WHERE storeProduct.storeGuid = :storeGuid)")
    List<ProductEntity> findAllProductNotInStore(UUID storeGuid);
}
