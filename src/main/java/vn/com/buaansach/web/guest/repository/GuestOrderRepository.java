package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.OrderEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findOneByGuid(UUID orderGuid);
}
