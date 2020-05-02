package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;

@Repository
public interface PosVoucherRepository extends JpaRepository<VoucherEntity, Long> {
}
