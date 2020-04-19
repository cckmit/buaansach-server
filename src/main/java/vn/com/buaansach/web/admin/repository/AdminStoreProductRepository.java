package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.StoreProductEntity;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminStoreProductRepository extends JpaRepository<StoreProductEntity, Long> {

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.read.AdminStoreProductDTO(storeProduct, product) " +
            "FROM StoreProductEntity storeProduct " +
            "LEFT JOIN vn.com.buaansach.entity.ProductEntity product " +
            "ON storeProduct.productGuid = product.guid " +
            "WHERE product.id IS NOT NULL " +
            "AND product.productStatus <> 'STOP_TRADING' " +
            "AND storeProduct.storeGuid = :storeGuid")
    List<AdminStoreProductDTO> findListAdminStoreProductDTO(@Param("storeGuid") UUID storeGuid);


    Optional<StoreProductEntity> findOneByGuid(UUID storeProductGuid);

    List<StoreProductEntity> findByProductGuid(UUID productGuid);
}
