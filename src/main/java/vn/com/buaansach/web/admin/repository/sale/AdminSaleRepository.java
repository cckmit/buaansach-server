package vn.com.buaansach.web.admin.repository.sale;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO;
import vn.com.buaansach.web.shared.repository.sale.SaleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminSaleRepository extends SaleRepository {
    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO(sale, time) " +
            "FROM SaleEntity sale " +
            "LEFT JOIN vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity time " +
            "ON sale.guid = time.saleGuid " +
            "WHERE sale.guid = :saleGuid")
    Optional<AdminSaleDTO> findOneDTOByGuid(@Param("saleGuid") UUID saleGuid);

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO(sale, time) " +
            "FROM SaleEntity sale " +
            "LEFT JOIN vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity time " +
            "ON sale.guid = time.saleGuid " +
            "ORDER BY sale.createdDate ASC")
    List<AdminSaleDTO> findListDTOByGuid();

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO(sale, time) " +
            "FROM SaleEntity sale " +
            "JOIN vn.com.buaansach.entity.sale.StoreSaleEntity storeSale " +
            "ON sale.guid = storeSale.saleGuid " +
            "LEFT JOIN vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity time " +
            "ON sale.guid = time.saleGuid " +
            "WHERE storeSale.storeGuid = :storeGuid " +
            "ORDER BY sale.createdDate ASC")
    List<AdminSaleDTO> findListDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);
}
