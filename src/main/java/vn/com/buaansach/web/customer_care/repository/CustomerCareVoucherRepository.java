package vn.com.buaansach.web.customer_care.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerCareVoucherRepository extends JpaRepository<VoucherEntity, Long> {
    Optional<VoucherEntity> findOneByGuid(UUID voucherGuid);
}
