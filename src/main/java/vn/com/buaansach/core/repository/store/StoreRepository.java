package vn.com.buaansach.core.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findOneByGuid(UUID storeGuid);

    Optional<StoreEntity> findOneByStoreCode(String storeCode);
}
