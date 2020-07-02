package vn.com.buaansach.web.customer_care.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherInventoryEntity;

@Repository
public interface CustomerCareVoucherInventoryRepository extends JpaRepository<VoucherInventoryEntity, Long> {
}
