package vn.com.buaansach.web.shared.repository.sale.condition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaleTimeConditionRepository extends JpaRepository<SaleTimeConditionEntity, Long> {

    Optional<SaleTimeConditionEntity> findOneBySaleGuid(UUID saleGuid);

    void deleteBySaleGuid(UUID saleGuid);
}
