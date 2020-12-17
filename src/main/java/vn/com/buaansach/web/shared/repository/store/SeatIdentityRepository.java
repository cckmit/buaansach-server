package vn.com.buaansach.web.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.SeatIdentityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeatIdentityRepository extends JpaRepository<SeatIdentityEntity, Long> {

    Optional<SeatIdentityEntity> findOneBySeatGuid(UUID seatGuid);

    List<SeatIdentityEntity> findBySeatGuidIn(List<UUID> listSeatGuid);
}
