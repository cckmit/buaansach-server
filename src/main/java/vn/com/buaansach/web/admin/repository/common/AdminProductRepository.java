package vn.com.buaansach.web.admin.repository.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.shared.repository.common.ProductRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminProductRepository extends ProductRepository {
    @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:search% OR p.productCode LIKE %:search%")
    Page<ProductEntity> findPageProductWithKeyword(Pageable pageable, @Param("search") String search);

    @Query("SELECT product FROM ProductEntity product " +
            "WHERE product.productStatus <> :productStatus " +
            "AND product.guid NOT IN (" +
            "SELECT DISTINCT storeProduct.productGuid " +
            "FROM vn.com.buaansach.entity.store.StoreProductEntity storeProduct " +
            "WHERE storeProduct.storeGuid = :storeGuid)")
    List<ProductEntity> findAllProductNotInStoreExceptStatus(@Param("storeGuid") UUID storeGuid,
                                                             @Param("productStatus") ProductStatus productStatus);

    @Query("SELECT MAX(pe.productPosition) FROM ProductEntity pe")
    Integer findLastProductPosition();

    @Modifying
    @Query(value = "UPDATE bas_product SET product_position = :pos  WHERE guid = :productGuid", nativeQuery = true)
    void updatePosition(@Param("productGuid") UUID productGuid, @Param("pos") int pos);

    List<ProductEntity> findAllByOrderByProductPositionAsc();
}
