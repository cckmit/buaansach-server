package vn.com.buaansach.web.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findOneByGuid(UUID productGuid);

    Optional<ProductEntity> findOneByProductCode(String productCode);

    @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:search% OR p.productCode LIKE %:search%")
    Page<ProductEntity> findPageProductWithKeyword(Pageable pageable, @Param("search") String search);

    @Query("SELECT product FROM ProductEntity product " +
            "WHERE product.productStatus <> :productStatus " +
            "AND product.guid NOT IN (" +
            "SELECT DISTINCT storeProduct.productGuid " +
            "FROM vn.com.buaansach.entity.store.StoreProductEntity storeProduct " +
            "WHERE storeProduct.storeGuid = :storeGuid)")
    List<ProductEntity> findAllProductNotInStoreExcept(@Param("storeGuid") UUID storeGuid, @Param("productStatus") ProductStatus productStatus);

    @Query("SELECT MAX(pe.productPosition) FROM ProductEntity pe")
    Integer findLastProductPosition();

    @Query(value = "SELECT p.product_code FROM bas_product p ORDER BY p.id DESC LIMIT 1", nativeQuery = true)
    String findLastProductCode();
}
