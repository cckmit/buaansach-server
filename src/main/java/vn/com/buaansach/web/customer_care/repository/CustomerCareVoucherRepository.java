package vn.com.buaansach.web.customer_care.repository;

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
public interface CustomerCareVoucherRepository extends JpaRepository<VoucherEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ve FROM VoucherEntity ve WHERE ve.id = :id")
    Optional<VoucherEntity> selectForUpdate(@Param("id") Long id);

    Optional<VoucherEntity> findOneByGuid(UUID voucherGuid);

}
