package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.web.guest.service.dto.GuestSeatDTO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestSeatRepository extends JpaRepository<SeatEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.guest.service.dto.GuestSeatDTO(store, area, seat) " +
            "FROM vn.com.buaansach.entity.SeatEntity seat " +
            "LEFT JOIN vn.com.buaansach.entity.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "LEFT JOIN vn.com.buaansach.entity.StoreEntity store " +
            "ON area.storeGuid = store.guid " +
            "WHERE seat.guid = :seatGuid")
    Optional<GuestSeatDTO> findGuestSeatDTO(@Param("seatGuid") UUID seatGuid);

    Optional<SeatEntity> findOneByGuid(UUID seatGuid);
}
