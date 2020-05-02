package vn.com.buaansach.web.admin.repository.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminVoucherCodeRepository extends JpaRepository<VoucherCodeEntity, Long> {
    List<VoucherCodeEntity> findByVoucherGuid(UUID voucherGuid);
}
