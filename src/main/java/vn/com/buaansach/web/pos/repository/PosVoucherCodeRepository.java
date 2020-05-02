package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;

@Repository
public interface PosVoucherCodeRepository extends JpaRepository<VoucherCodeEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO(vc, v, vt, vu) " +
            "FROM VoucherCodeEntity vc " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.VoucherEntity v " +
            "ON vc.voucherGuid = v.guid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity vt " +
            "ON vt.voucherGuid = v.guid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity vu " +
            "ON vu.voucherGuid = v.guid " +
            "WHERE vc.voucherCode = :voucherCode")
    PosVoucherCodeDTO getPosVoucherCodeDTO(@Param("voucherCode") String voucherCode);
}
