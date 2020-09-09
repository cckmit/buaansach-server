package vn.com.buaansach.web.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findOneByGuid(UUID storeGuid);

    Optional<StoreEntity> findOneByStoreCode(String storeCode);

    List<StoreEntity> findByStorePrimarySaleGuid(UUID saleGuid);
}
