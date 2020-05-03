package vn.com.buaansach.web.admin.repository.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminVoucherDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminVoucherRepository extends JpaRepository<VoucherEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.readwrite.AdminVoucherDTO(voucher, time, usage, store) " +
            "FROM VoucherEntity voucher " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity time " +
            "ON voucher.guid = time.voucherGuid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity usage " +
            "ON voucher.guid = usage.voucherGuid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherStoreConditionEntity store " +
            "ON voucher.guid = store.voucherGuid " +
            "ORDER BY voucher.id ASC")
    List<AdminVoucherDTO> findListAdminVoucherDTO();

    Optional<VoucherEntity> findOneByGuid(UUID voucherGuid);
}
