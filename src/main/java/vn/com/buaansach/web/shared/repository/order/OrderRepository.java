package vn.com.buaansach.web.shared.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderEntity;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findBySeatGuidIn(List<UUID> listSeatGuid);

    List<OrderEntity> findBySeatGuid(UUID seatGuid);

    Optional<OrderEntity> findOneByGuid(UUID fromString);

    List<OrderEntity> findByGuidIn(List<UUID> listOrderGuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OrderEntity> findTop1ByStoreGuidOrderByIdDesc(UUID guid);

    @Lock(LockModeType.READ)
    int countByStoreGuid(UUID storeGuid);
}
