package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderFeedbackEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestOrderFeedbackRepository extends JpaRepository<OrderFeedbackEntity, Long> {
    Optional<OrderFeedbackEntity> findOneByOrderGuid(UUID orderGuid);
}
