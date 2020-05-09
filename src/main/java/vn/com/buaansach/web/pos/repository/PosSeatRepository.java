package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosSeatRepository extends JpaRepository<SeatEntity, Long> {

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO(seat, area) " +
            "FROM vn.com.buaansach.entity.store.SeatEntity seat " +
            "LEFT JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "LEFT JOIN vn.com.buaansach.entity.store.StoreEntity store " +
            "ON area.storeGuid = store.guid " +
            "WHERE store.guid = :storeGuid")
    List<PosSeatDTO> findListPosSeatDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO(seat, area) " +
            "FROM vn.com.buaansach.entity.store.SeatEntity seat " +
            "LEFT JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "WHERE seat.guid = :seatGuid")
    Optional<PosSeatDTO> findPosSeatDTOBySeatGuid(@Param("seatGuid") UUID seatGuid);

    Optional<SeatEntity> findOneByGuid(UUID seatGuid);

    Optional<SeatEntity> findOneByCurrentOrderGuid(UUID orderGuid);
}
