package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerEntity;

import java.util.Optional;

@Repository
public interface PosCustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query(value = "SELECT c.customer_code FROM bas_customer c ORDER BY c.id DESC LIMIT 1", nativeQuery = true)
    String findLastCustomerCode();

    Optional<CustomerEntity> findOneByCustomerPhone(String customerPhone);
}
