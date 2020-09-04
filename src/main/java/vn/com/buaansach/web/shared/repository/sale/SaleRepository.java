package vn.com.buaansach.web.shared.repository.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.sale.SaleEntity;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Long> {

}
