package vn.com.buaansach.core.repository.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.sale.StoreSaleEntity;

@Repository
public interface StoreSaleRepository extends JpaRepository<StoreSaleEntity, Long> {

}
