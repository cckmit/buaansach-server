package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.store.StoreRepository;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminStoreRepository extends StoreRepository {
    @Query("SELECT s FROM StoreEntity s WHERE s.storeName LIKE %:search% OR s.storeCode LIKE %:search%")
    Page<StoreEntity> findPageStoreWithKeyword(Pageable pageable, @Param("search") String search);

    @Query(value = "SELECT s.store_code FROM bas_store s ORDER BY s.id DESC LIMIT 1", nativeQuery = true)
    String findLastStoreCode();

    @Query("SELECT store FROM StoreEntity store " +
            "JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON store.guid = area.storeGuid " +
            "JOIN vn.com.buaansach.entity.store.SeatEntity seat " +
            "ON area.guid = seat.areaGuid " +
            "WHERE seat.guid = :seatGuid")
    Optional<StoreEntity> findOneBySeatGuid(@Param("seatGuid") UUID seatGuid);
}
