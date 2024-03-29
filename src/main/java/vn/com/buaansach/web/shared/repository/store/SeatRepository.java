package vn.com.buaansach.web.shared.repository.store;

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

    List<SeatEntity> findByGuidIn(List<UUID> listSeatGuid);

    void deleteByAreaGuid(UUID areaGuid);

    void deleteByGuid(UUID seatGuid);

    List<SeatEntity> findByGuidNotInOrderByIdAsc(List<UUID> listSeatGuid);
}
