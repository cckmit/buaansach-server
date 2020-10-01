package vn.com.buaansach.web.shared.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.customer.CustomerPointLogEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerPointLogRepository extends JpaRepository<CustomerPointLogEntity, Long> {

    List<CustomerPointLogEntity> findTop20ByUserGuidOrderByIdDesc(UUID userGuid);
}
