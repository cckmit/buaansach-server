package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerEntity;

import java.util.Optional;

@Repository
public interface GuestCustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findOneByCustomerPhone(String customerPhone);
}
