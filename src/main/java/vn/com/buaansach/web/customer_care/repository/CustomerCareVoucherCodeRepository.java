package vn.com.buaansach.web.customer_care.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareVoucherCodeDTO;

import java.util.Optional;

@Repository
public interface CustomerCareVoucherCodeRepository extends JpaRepository<VoucherCodeEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.customer_care.service.dto.read.CustomerCareVoucherCodeDTO(vc, v, vt, vu, vs) " +
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
    Optional<CustomerCareVoucherCodeDTO> getCustomerCareVoucherCodeDTO(@Param("voucherCode") String voucherCode);

    Optional<VoucherCodeEntity> findOneByVoucherCode(String voucherCode);
}
