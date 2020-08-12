package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.store.StoreOrderEntity;
import vn.com.buaansach.entity.store.StorePayRequestEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PosStorePayRequestRepository extends JpaRepository<StorePayRequestEntity, Long> {
    List<StorePayRequestEntity> findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID storeGuid, Instant from);

    List<StorePayRequestEntity> findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID storeGuid, Instant from, boolean hidden);

    Optional<StorePayRequestEntity> findOneByGuid(UUID guid);

    List<StorePayRequestEntity> findByGuidIn(List<UUID> listGuid);

    List<StorePayRequestEntity> findByCreatedDateBefore(Instant before);

    Optional<StorePayRequestEntity> findOneByOrderGuid(UUID orderGuid);
}
