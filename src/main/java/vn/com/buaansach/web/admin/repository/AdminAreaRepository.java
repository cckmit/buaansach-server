package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.AreaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminAreaRepository extends JpaRepository<AreaEntity, Long> {
    Optional<AreaEntity> findOneByGuid(UUID guid);

    List<AreaEntity> findByStoreGuid(UUID storeGuid);

    void deleteByStoreGuid(UUID storeGuid);
}
