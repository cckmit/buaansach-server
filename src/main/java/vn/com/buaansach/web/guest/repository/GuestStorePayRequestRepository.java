package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.store.StorePayRequestEntity;

import java.util.Optional;
import java.util.UUID;

public interface GuestStorePayRequestRepository extends JpaRepository<StorePayRequestEntity, Long> {
    Optional<StorePayRequestEntity> findOneByOrderGuid(UUID orderGuid);
}
