package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreOrderEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosStoreOrderRepository extends JpaRepository<StoreOrderEntity, Long> {
    List<StoreOrderEntity> findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID storeGuid, Instant from);

    List<StoreOrderEntity> findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID storeGuid, Instant from, boolean hidden);

    Optional<StoreOrderEntity> findOneByGuid(UUID guid);

    List<StoreOrderEntity> findByGuidIn(List<UUID> listGuid);

    List<StoreOrderEntity> findByCreatedDateBefore(Instant before);

    List<StoreOrderEntity> findByOrderGuid(UUID orderGuid);
}
