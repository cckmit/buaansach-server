package vn.com.buaansach.core.repository.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.StoreVoucherEntity;

@Repository
public interface StoreVoucherRepository extends JpaRepository<StoreVoucherEntity, Long> {

}
