package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.service.dto.EmployeeStoreUserDTO;
import vn.com.buaansach.service.dto.guest.GuestSeatDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    @Query(value = "SELECT s.* FROM bas_seat s WHERE s.area_id IN (SELECT a.id FROM bas_area a WHERE a.store_id = :storeId)", nativeQuery = true)
    List<SeatEntity> findSeatEntitiesByStoreId(@Param("storeId") Long storeId);

    Optional<SeatEntity> findOneByGuid(UUID guid);

    List<SeatEntity> findByAreaId(Long areaId);

    void deleteByAreaId(Long areaId);

    void deleteByGuid(UUID guid);

    @Query("SELECT new vn.com.buaansach.service.dto.guest.GuestSeatDTO(store, area, seat) " +
            "FROM vn.com.buaansach.entity.SeatEntity seat " +
            "LEFT JOIN vn.com.buaansach.entity.AreaEntity area " +
            "ON seat.areaId = area.id " +
            "LEFT JOIN vn.com.buaansach.entity.StoreEntity store " +
            "ON area.storeId = store.id " +
            "WHERE seat.guid = :seatGuid")
    Optional<GuestSeatDTO> findGuestSeatDTO(@Param("seatGuid") UUID seatGuid);
}
