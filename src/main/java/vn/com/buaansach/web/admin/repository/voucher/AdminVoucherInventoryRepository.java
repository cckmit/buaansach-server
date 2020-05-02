package vn.com.buaansach.web.admin.repository.voucher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import vn.com.buaansach.entity.voucher.VoucherInventoryEntity;

import javax.persistence.LockModeType;

public interface AdminVoucherInventoryRepository extends JpaRepository<VoucherInventoryEntity, Long> {
    @Query("SELECT COUNT(e) FROM VoucherInventoryEntity e")
    int countAll();

    int countByExportedFalse();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT vie FROM VoucherInventoryEntity vie WHERE vie.exported = FALSE")
    Page<VoucherInventoryEntity> getListUnExportedVoucherInventory(Pageable pageable);
}
