package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.SeatEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminSeatRepository extends JpaRepository<SeatEntity, Long> {

    @Query(value = "SELECT s.* FROM bas_seat s WHERE s.area_guid IN (SELECT a.guid FROM bas_area a WHERE a.store_guid = :storeGuid)", nativeQuery = true)
    List<SeatEntity> findListSeatByStoreGuid(@Param("storeGuid") UUID storeGuid);

    Optional<SeatEntity> findOneByGuid(UUID guid);

    List<SeatEntity> findByAreaGuid(UUID areaGuid);

    long deleteByAreaGuid(UUID areaGuid);

    @Override
    void delete(SeatEntity entity);
}
