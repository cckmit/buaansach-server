package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.web.pos.service.dto.PosSeatDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosSeatRepository extends JpaRepository<SeatEntity, Long> {

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.PosSeatDTO(seat, area) " +
            "FROM vn.com.buaansach.entity.SeatEntity seat " +
            "LEFT JOIN vn.com.buaansach.entity.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "LEFT JOIN vn.com.buaansach.entity.StoreEntity store " +
            "ON area.storeGuid = store.guid " +
            "WHERE store.guid = :storeGuid")
    List<PosSeatDTO> findListPosSeatDTOByStoreGuid(@Param("storeGuid") UUID storeGuid);

}
