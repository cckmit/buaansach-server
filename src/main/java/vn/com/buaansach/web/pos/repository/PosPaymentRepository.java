package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.PaymentEntity;

@Repository
public interface PosPaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
