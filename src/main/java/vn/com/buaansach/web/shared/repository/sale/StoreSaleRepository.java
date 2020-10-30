package vn.com.buaansach.web.shared.repository.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.sale.StoreSaleEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreSaleRepository extends JpaRepository<StoreSaleEntity, Long> {

    Optional<StoreSaleEntity> findOneByStoreGuidAndSaleGuid(UUID storeGuid, UUID saleGuid);

    void deleteByGuid(UUID guid);

    Optional<StoreSaleEntity> findOneByGuid(UUID guid);

    List<StoreSaleEntity> findBySaleGuid(UUID saleGuid);

    void deleteByStoreGuid(UUID storeGuid);
}
