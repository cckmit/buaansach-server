package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.AreaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosAreaRepository extends JpaRepository<AreaEntity, Long> {
    List<AreaEntity> findByStoreGuid(UUID storeGuid);

    Optional<AreaEntity> findOneByGuid(UUID areaGuid);
}
