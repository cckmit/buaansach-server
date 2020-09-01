package vn.com.buaansach.core.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findBySeatGuidIn(List<UUID> listSeatGuid);

    List<OrderEntity> findBySeatGuid(UUID seatGuid);

    Optional<OrderEntity> findOneByGuid(UUID fromString);
}
