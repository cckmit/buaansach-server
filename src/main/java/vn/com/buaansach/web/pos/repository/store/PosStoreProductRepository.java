package vn.com.buaansach.web.pos.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.core.repository.store.StoreProductRepository;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosStoreProductRepository extends StoreProductRepository {
    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND storeProduct.storeProductStatus <> :storeProductStatus " +
            "AND storeProduct.storeGuid = :storeGuid")
    List<PosStoreProductDTO> findListPosStoreProductDTO(@Param("storeGuid") UUID storeGuid,
                                                        @Param("storeProductStatus") StoreProductStatus storeProductStatus);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductCategoryEntity productCategory " +
            "ON productCategory.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND storeProduct.storeProductStatus <> :storeProductStatus " +
            "AND storeProduct.storeGuid = :storeGuid " +
            "AND productCategory.categoryGuid = :categoryGuid " +
            "ORDER BY product.productPosition ASC")
    List<PosStoreProductDTO> findListPosStoreProductByStoreAndCategoryExceptStatus(@Param("storeGuid") UUID storeGuid,
                                                                                   @Param("categoryGuid") UUID categoryGuid,
                                                                                   @Param("storeProductStatus") StoreProductStatus storeProductStatus);
}
