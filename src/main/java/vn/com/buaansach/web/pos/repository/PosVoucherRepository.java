package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosVoucherRepository extends JpaRepository<VoucherEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ve FROM VoucherEntity ve WHERE ve.id = :id")
    Optional<VoucherEntity> selectForUpdate(@Param("id") Long id);

    Optional<VoucherEntity> findOneByGuid(UUID voucherGuid);

    @Query("SELECT ve FROM VoucherEntity ve " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.VoucherCodeEntity vce " +
            "ON ve.guid = vce.voucherGuid " +
            "WHERE vce.voucherCode = :voucherCode")
    Optional<VoucherEntity> findOneByVoucherCode(@Param("voucherCode") String voucherCode);
}
