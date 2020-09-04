package vn.com.buaansach.shared.repository.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.sale.SaleUsageEntity;

@Repository
public interface SaleUsageRepository extends JpaRepository<SaleUsageEntity, Long> {

}
