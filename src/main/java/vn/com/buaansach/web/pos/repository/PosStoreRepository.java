package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.StoreEntity;

import java.util.Optional;
import java.util.UUID;

public interface PosStoreRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findOneByGuid(UUID storeGuid);
}
