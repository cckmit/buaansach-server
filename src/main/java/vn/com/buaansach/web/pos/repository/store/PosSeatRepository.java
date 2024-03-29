package vn.com.buaansach.web.pos.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.store.SeatRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosSeatRepository extends SeatRepository {

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO(seat, area) " +
            "FROM vn.com.buaansach.entity.store.SeatEntity seat " +
            "JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON area.storeGuid = store.guid " +
            "WHERE store.guid = :storeGuid")
    List<PosSeatDTO> findListPosSeatDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO(seat, area) " +
            "FROM vn.com.buaansach.entity.store.SeatEntity seat " +
            "JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "WHERE seat.guid = :seatGuid")
    Optional<PosSeatDTO> findPosSeatDTOBySeatGuid(@Param("seatGuid") UUID seatGuid);

}
