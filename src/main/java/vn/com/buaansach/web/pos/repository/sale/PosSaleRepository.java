package vn.com.buaansach.web.pos.repository.sale;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleDTO;
import vn.com.buaansach.web.shared.repository.sale.SaleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosSaleRepository extends SaleRepository {
    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosSaleDTO(sale, time) " +
            "FROM SaleEntity sale " +
            "LEFT JOIN vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity time " +
            "ON sale.guid = time.saleGuid " +
            "WHERE sale.guid = :saleGuid")
    Optional<PosSaleDTO> findOneDTOByGuid(@Param("saleGuid") UUID saleGuid);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosSaleDTO(sale, time) " +
            "FROM SaleEntity sale " +
            "JOIN vn.com.buaansach.entity.sale.StoreSaleEntity storeSale " +
            "ON sale.guid = storeSale.saleGuid " +
            "LEFT JOIN vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity time " +
            "ON sale.guid = time.saleGuid " +
            "WHERE storeSale.storeGuid = :storeGuid " +
            "AND sale.saleActivated = TRUE " +
            "ORDER BY sale.createdDate ASC")
    List<PosSaleDTO> findListDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);
}
