package vn.com.buaansach.web.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.AreaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, Long> {
    Optional<AreaEntity> findOneByGuid(UUID guid);

    List<AreaEntity> findByStoreGuid(UUID storeGuid);

    List<AreaEntity> findByStoreGuidOrderByAreaPositionAsc(UUID storeGuid);

    void deleteByStoreGuid(UUID storeGuid);

    List<AreaEntity> findByStoreGuidAndAreaActivated(UUID storeGuid, boolean areaActivated);

    List<AreaEntity> findByStoreGuidAndAreaActivatedOrderByAreaPositionAsc(UUID storeGuid, boolean areaActivated);
}
