package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.entity.SeatEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminSeatRepository extends JpaRepository<SeatEntity, Long> {

    @Query(value = "SELECT s.* FROM bas_seat s WHERE s.area_id IN (SELECT a.id FROM bas_area a WHERE a.store_id = :storeId)", nativeQuery = true)
    List<SeatEntity> findListSeatByStoreId(@Param("storeId") Long storeId);

    Optional<SeatEntity> findOneByGuid(UUID guid);

    List<SeatEntity> findByAreaId(Long areaId);
}
