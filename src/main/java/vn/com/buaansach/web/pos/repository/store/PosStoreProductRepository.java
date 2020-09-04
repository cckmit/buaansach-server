package vn.com.buaansach.web.pos.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.shared.repository.store.StoreProductRepository;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosStoreProductRepository extends StoreProductRepository {
    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "JOIN vn.com.buaansach.entity.common.ProductCategoryEntity productCategory " +
            "ON product.guid = productCategory.productGuid " +
            "WHERE storeProduct.storeGuid = :storeGuid " +
            "AND productCategory.categoryGuid = :categoryGuid " +
            "AND storeProduct.storeProductStatus <> :storeProductStatus " +
            "ORDER BY product.productPosition ASC")
    List<PosStoreProductDTO> findListPosStoreProductByStoreAndCategoryExceptStatus(@Param("storeGuid") UUID storeGuid,
                                                                                   @Param("categoryGuid") UUID categoryGuid,
                                                                                   @Param("storeProductStatus") StoreProductStatus storeProductStatus);
}
