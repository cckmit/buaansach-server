package vn.com.buaansach.web.customer_care.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerCareOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findOneByGuid(UUID fromString);
}
