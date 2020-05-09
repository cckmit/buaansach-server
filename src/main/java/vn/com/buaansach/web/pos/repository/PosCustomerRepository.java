package vn.com.buaansach.web.pos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerEntity;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface PosCustomerRepository extends JpaRepository<CustomerEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT ce FROM CustomerEntity ce")
    Page<CustomerEntity> findPageCustomer(Pageable pageable);

    Optional<CustomerEntity> findOneByCustomerPhone(String customerPhone);
}
