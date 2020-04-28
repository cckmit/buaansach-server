package vn.com.buaansach.web.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.StoreProductEntity;
import vn.com.buaansach.web.customer.service.dto.CustomerStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerStoreProductRepository extends JpaRepository<StoreProductEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.customer.service.dto.CustomerStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND product.productStatus <> 'STOP_TRADING' " +
            "AND storeProduct.storeGuid = :storeGuid")
    List<CustomerStoreProductDTO> findListCustomerStoreProductDTO(@Param("storeGuid") UUID storeGuid);
}
