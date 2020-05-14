package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosStoreProductRepository extends JpaRepository<StoreProductEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND storeProduct.storeProductStatus <> 'STOP_TRADING' " +
            "AND storeProduct.storeGuid = :storeGuid")
    List<PosStoreProductDTO> findListPosStoreProductDTO(@Param("storeGuid") UUID storeGuid);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductCategoryEntity productCategory " +
            "ON productCategory.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND storeProduct.storeProductStatus <> 'STOP_TRADING' " +
            "AND storeProduct.storeGuid = :storeGuid " +
            "AND productCategory.categoryGuid = :categoryGuid " +
            "ORDER BY product.productPosition ASC")
    List<PosStoreProductDTO> findListPosStoreProductByStoreAndCategory(@Param("storeGuid") UUID storeGuid, @Param("categoryGuid") UUID categoryGuid);

    Optional<StoreProductEntity> findOneByGuid(UUID storeProductGuid);
}
