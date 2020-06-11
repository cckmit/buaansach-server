package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestAreaRepository extends JpaRepository<AreaEntity, Long> {
    Optional<AreaEntity> findOneByGuid(UUID seatGuid);
}
