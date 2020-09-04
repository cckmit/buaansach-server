package vn.com.buaansach.shared.repository.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;

import java.util.Optional;

@Repository
public interface VoucherCodeRepository extends JpaRepository<VoucherCodeEntity, Long> {
    Optional<VoucherCodeEntity> findOneByVoucherCode(String voucherCode);

}
