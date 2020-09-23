package vn.com.buaansach.web.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findOneByGuid(UUID storeGuid);

    List<StoreEntity> findByStorePrimarySaleGuid(UUID saleGuid);

    @Query("SELECT store FROM StoreEntity store " +
            "JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON store.guid = area.storeGuid " +
            "JOIN vn.com.buaansach.entity.store.SeatEntity seat " +
            "ON area.guid = seat.areaGuid " +
            "WHERE seat.guid = :seatGuid")
    Optional<StoreEntity> findOneBySeatGuid(@Param("seatGuid") UUID seatGuid);
}
