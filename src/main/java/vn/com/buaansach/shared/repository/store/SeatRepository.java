package vn.com.buaansach.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.SeatEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    Optional<SeatEntity> findOneByGuid(UUID guid);

    List<SeatEntity> findByAreaGuid(UUID areaGuid);

    Optional<SeatEntity> findOneByOrderGuid(UUID orderGuid);
}
