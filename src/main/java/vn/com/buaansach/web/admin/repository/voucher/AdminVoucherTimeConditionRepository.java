package vn.com.buaansach.web.admin.repository.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity;
import vn.com.buaansach.web.shared.repository.voucher.condition.VoucherTimeConditionRepository;

@Repository
public interface AdminVoucherTimeConditionRepository extends VoucherTimeConditionRepository {
}
