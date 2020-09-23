package vn.com.buaansach.web.shared.repository.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.web.shared.service.dto.read.SaleDTO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Long> {

    Optional<SaleEntity> findOneByGuid(UUID saleGuid);

    void deleteByGuid(UUID saleGuid);

    @Query("SELECT new vn.com.buaansach.web.shared.service.dto.read.SaleDTO(sale, time) " +
            "FROM SaleEntity sale " +
            "LEFT JOIN vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity time " +
            "ON sale.guid = time.saleGuid " +
            "WHERE sale.guid = :saleGuid")
    Optional<SaleDTO> findOneSaleDTOByGuid(@Param("saleGuid") UUID saleGuid);
}
