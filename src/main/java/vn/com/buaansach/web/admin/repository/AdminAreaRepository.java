package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.AreaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminAreaRepository extends JpaRepository<AreaEntity, Long> {
    Optional<AreaEntity> findOneByGuid(UUID guid);

    List<AreaEntity> findByStoreId(Long storeId);
}
