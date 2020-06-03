package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminOrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findBySeatGuidIn(List<UUID> listSeatGuid);

    List<OrderEntity> findBySeatGuid(UUID seatGuid);
}
