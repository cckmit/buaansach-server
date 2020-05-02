package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.VoucherInventoryEntity;

@Repository
public interface PosVoucherInventoryRepository extends JpaRepository<VoucherInventoryEntity, Long> {
}
