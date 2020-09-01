package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.core.repository.store.StoreProductRepository;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreProductRepository extends StoreProductRepository {

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND product.productStatus <> :productStatus " +
            "AND storeProduct.storeGuid = :storeGuid " +
            "ORDER BY product.productPosition ASC")
    List<AdminStoreProductDTO> findListAdminStoreProductDTOExcept(@Param("storeGuid") UUID storeGuid, @Param("productStatus") ProductStatus productStatus);

}
