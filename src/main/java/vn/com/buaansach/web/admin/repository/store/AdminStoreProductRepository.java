package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.store.StoreProductRepository;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreProductRepository extends StoreProductRepository {

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "WHERE storeProduct.storeGuid = :storeGuid " +
            "ORDER BY product.productPosition ASC")
    List<AdminStoreProductDTO> findListAdminStoreProductDTOByStore(@Param("storeGuid") UUID storeGuid);

}
