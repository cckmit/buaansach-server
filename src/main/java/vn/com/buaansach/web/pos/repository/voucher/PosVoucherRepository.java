package vn.com.buaansach.web.pos.repository.voucher;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.voucher.VoucherRepository;
import vn.com.buaansach.entity.voucher.VoucherEntity;

import java.util.Optional;

@Repository
public interface PosVoucherRepository extends VoucherRepository {

    @Query("SELECT ve FROM VoucherEntity ve " +
            "LEFT JOIN vn.com.buaansach.entity.voucher.VoucherCodeEntity vce " +
            "ON ve.guid = vce.voucherGuid " +
            "WHERE vce.voucherCode = :voucherCode")
    Optional<VoucherEntity> findOneByVoucherCode(@Param("voucherCode") String voucherCode);
}
