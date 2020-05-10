package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuestStoreProductRepository extends JpaRepository<StoreProductEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND product.productStatus <> :productStatus " +
            "AND storeProduct.storeGuid = :storeGuid")
    List<GuestStoreProductDTO> findListGuestStoreProductDTOExceptStatus(@Param("storeGuid") UUID storeGuid, @Param("productStatus") ProductStatus productStatus);

    @Query("SELECT new vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductCategoryEntity productCategory " +
            "ON productCategory.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND product.productStatus <> :productStatus " +
            "AND storeProduct.storeGuid = :storeGuid " +
            "AND productCategory.categoryGuid = :categoryGuid " +
            "ORDER BY product.productPosition ASC")
    List<GuestStoreProductDTO> findListGuestStoreProductByStoreAndCategoryExceptStatus(@Param("storeGuid") UUID storeGuid, @Param("categoryGuid") UUID categoryGuid, @Param("productStatus") ProductStatus productStatus);
}
