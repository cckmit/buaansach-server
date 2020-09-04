package vn.com.buaansach.web.guest.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.shared.repository.store.SeatRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestSeatRepository extends SeatRepository {
    @Query("SELECT new vn.com.buaansach.web.guest.service.dto.read.GuestSeatDTO(store, area, seat) " +
            "FROM SeatEntity seat " +
            "JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON area.storeGuid = store.guid " +
            "WHERE seat.guid = :seatGuid")
    Optional<GuestSeatDTO> findGuestSeatDTO(@Param("seatGuid") UUID seatGuid);
}
