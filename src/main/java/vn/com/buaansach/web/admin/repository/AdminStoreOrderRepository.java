package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreOrderEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreOrderRepository extends JpaRepository<StoreOrderEntity, Long> {
    List<StoreOrderEntity> findByStoreGuid(UUID storeGuid);
    List<StoreOrderEntity> findByAreaGuid(UUID areaGuid);
    List<StoreOrderEntity> findBySeatGuid(UUID seatGuid);
}
