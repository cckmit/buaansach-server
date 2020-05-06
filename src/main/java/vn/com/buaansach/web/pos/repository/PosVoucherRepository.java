package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.voucher.VoucherEntity;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface PosVoucherRepository extends JpaRepository<VoucherEntity, Long> {

    @Modifying
    @Query("UPDATE VoucherEntity ve SET ve.numberVoucherCode = ve.numberVoucherCode + 1")
    void increaseNumberVoucherCode(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ve FROM VoucherEntity ve WHERE ve.id = :id")
    Optional<VoucherEntity> selectForUpdate(@Param("id") Long id);
}
