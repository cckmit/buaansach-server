package vn.com.buaansach.web.admin.repository.voucher;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminVoucherDTO;
import vn.com.buaansach.web.shared.repository.voucher.VoucherRepository;

import java.util.List;

@Repository
public interface AdminVoucherRepository extends VoucherRepository {
    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.readwrite.AdminVoucherDTO(voucher, time, usage) " +
            "FROM VoucherEntity voucher " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity time " +
            "ON voucher.guid = time.voucherGuid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity usage " +
            "ON voucher.guid = usage.voucherGuid " +
            "ORDER BY voucher.id ASC")
    List<AdminVoucherDTO> findListAdminVoucherDTO();
}
