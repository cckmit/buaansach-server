package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestVoucherCodeRepository extends JpaRepository<VoucherCodeEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO(vc, v, vt, vu, vs) " +
            "FROM VoucherCodeEntity vc " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.VoucherEntity v " +
            "ON vc.voucherGuid = v.guid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity vt " +
            "ON vt.voucherGuid = v.guid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity vu " +
            "ON vu.voucherGuid = v.guid " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.condition.VoucherStoreConditionEntity vs " +
            "ON vs.voucherGuid = v.guid " +
            "WHERE vc.voucherCode = :voucherCode")
    Optional<PosVoucherCodeDTO> getPosVoucherCodeDTO(@Param("voucherCode") String voucherCode);

    Optional<VoucherCodeEntity> findOneByVoucherCode(String voucherCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT vc FROM VoucherCodeEntity vc WHERE vc.voucherCode = :voucherCode")
    Optional<VoucherCodeEntity> findOneByVoucherCodeForUpdate(@Param("voucherCode") String voucherCode);

    @Query("SELECT COUNT(vc) FROM VoucherCodeEntity vc WHERE vc.voucherGuid = :voucherGuid")
    int countNumberVoucherCodeByVoucherGuid(@Param("voucherGuid") UUID voucherGuid);
}