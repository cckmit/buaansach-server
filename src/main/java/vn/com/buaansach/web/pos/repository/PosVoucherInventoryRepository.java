package vn.com.buaansach.web.pos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.VoucherInventoryEntity;

import javax.persistence.LockModeType;

@Repository
public interface PosVoucherInventoryRepository extends JpaRepository<VoucherInventoryEntity, Long> {
    int countByExportedFalse();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT vie FROM VoucherInventoryEntity vie WHERE vie.exported = FALSE")
    Page<VoucherInventoryEntity> getListUnExportedVoucherInventory(Pageable pageable);
}
