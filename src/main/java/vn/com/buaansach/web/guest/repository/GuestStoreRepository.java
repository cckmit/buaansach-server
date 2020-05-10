package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestStoreRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findOneByGuid(UUID storeGuid);
}
