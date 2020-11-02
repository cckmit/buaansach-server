package vn.com.buaansach.web.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreWorkShiftEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreWorkShiftRepository extends JpaRepository<StoreWorkShiftEntity, Long> {

    Optional<StoreWorkShiftEntity> findOneByGuid(UUID guid);

    void deleteByGuid(UUID storeWorkShiftGuid);

    List<StoreWorkShiftEntity> findByStoreGuid(UUID storeGuid);
}
