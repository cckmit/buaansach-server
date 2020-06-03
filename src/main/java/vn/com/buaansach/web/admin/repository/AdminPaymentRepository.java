package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.PaymentEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByOrderGuidIn(List<UUID> listOrderGuid);
}
