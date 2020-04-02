package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.AreaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AreaRepository extends JpaRepository<AreaEntity, Long> {
    Optional<AreaEntity> findOneByGuid(UUID guid);

    List<AreaEntity> findByStoreId(Long storeId);

    List<AreaEntity> findByStoreIdAndAreaName(Long storeId, String areaName);

    void deleteByGuid(UUID guid);
}
