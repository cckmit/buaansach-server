package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.core.repository.store.SeatRepository;
import vn.com.buaansach.entity.store.SeatEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminSeatRepository extends SeatRepository {

    @Query(value = "SELECT s.* FROM bas_seat s " +
            "WHERE s.area_guid IN " +
            "(SELECT a.guid FROM bas_area a WHERE a.store_guid = :storeGuid)", nativeQuery = true)
    List<SeatEntity> findListSeatByStoreGuid(@Param("storeGuid") UUID storeGuid);
}
