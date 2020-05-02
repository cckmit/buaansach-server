package vn.com.buaansach.web.admin.repository.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity;

@Repository
public interface AdminVoucherUsageConditionRepository extends JpaRepository<VoucherUsageConditionEntity, Long> {
}
