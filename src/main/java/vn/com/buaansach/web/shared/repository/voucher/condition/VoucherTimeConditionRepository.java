package vn.com.buaansach.web.shared.repository.voucher.condition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity;

@Repository
public interface VoucherTimeConditionRepository extends JpaRepository<VoucherTimeConditionEntity, Long> {

}
