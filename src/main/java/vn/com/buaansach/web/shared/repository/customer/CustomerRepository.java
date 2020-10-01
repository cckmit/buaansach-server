package vn.com.buaansach.web.shared.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findOneByUserGuid(UUID userGuid);

    @Query("SELECT customer FROM CustomerEntity customer " +
            "JOIN vn.com.buaansach.entity.user.UserEntity user " +
            "ON customer.userGuid = user.guid " +
            "WHERE user.userPhone = :userPhone ")
    Optional<CustomerEntity> findOneByUserPhone(@Param("userPhone") String userPhone);
}
