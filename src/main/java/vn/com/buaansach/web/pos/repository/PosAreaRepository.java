package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.AreaEntity;

import java.util.List;

public interface PosAreaRepository extends JpaRepository<AreaEntity, Long> {
    List<AreaEntity> findByStoreId(Long storeId);
}
