package vn.com.buaansach.web.admin.repository.sale;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.admin.service.dto.write.AdminStoreSaleDTO;
import vn.com.buaansach.web.shared.repository.sale.StoreSaleRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminStoreSaleRepository extends StoreSaleRepository {

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.write.AdminStoreSaleDTO(storeSale, sale, timeCondition) " +
            "FROM StoreSaleEntity storeSale " +
            "JOIN vn.com.buaansach.entity.sale.SaleEntity sale " +
            "ON storeSale.saleGuid = sale.guid " +
            "LEFT JOIN vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity timeCondition " +
            "ON sale.guid = timeCondition.saleGuid " +
            "WHERE storeSale.storeGuid = :storeGuid " +
            "ORDER BY sale.createdDate ASC")
    List<AdminStoreSaleDTO> findListAdminStoreSaleDTO(@Param("storeGuid") UUID storeGuid);
}
