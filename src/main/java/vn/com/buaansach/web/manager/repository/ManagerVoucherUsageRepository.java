package vn.com.buaansach.web.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherUsageEntity;

@Repository
public interface ManagerVoucherUsageRepository extends JpaRepository<VoucherUsageEntity, Long> {
}
