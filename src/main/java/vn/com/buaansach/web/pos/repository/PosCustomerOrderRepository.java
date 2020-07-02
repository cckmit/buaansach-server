package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerOrderEntity;

import java.util.List;

@Repository
public interface PosCustomerOrderRepository extends JpaRepository<CustomerOrderEntity, Long> {
    List<CustomerOrderEntity> findByCustomerPhone(String customerPhone);
}