package vn.com.buaansach.web.admin.repository.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherUsageEntity;

@Repository
public interface AdminVoucherUsageRepository extends JpaRepository<VoucherUsageEntity, Long> {
}
