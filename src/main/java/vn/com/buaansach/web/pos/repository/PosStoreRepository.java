package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosStoreRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findOneByGuid(UUID storeGuid);

    @Query("SELECT store FROM StoreEntity store " +
            "LEFT JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON store.guid = area.storeGuid " +
            "LEFT JOIN vn.com.buaansach.entity.store.SeatEntity seat " +
            "ON area.guid = seat.areaGuid " +
            "WHERE seat.guid = :seatGuid " +
            "AND store.storeActivated = TRUE ")
    Optional<StoreEntity> findOneBySeatGuid(@Param("seatGuid") UUID seatGuid);
}
