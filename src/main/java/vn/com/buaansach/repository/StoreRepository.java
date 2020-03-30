package vn.com.buaansach.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.entity.StoreEntity;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    @Query("SELECT s FROM StoreEntity s WHERE s.storeName LIKE %:search% OR s.storeCode LIKE %:search%")
    Page<StoreEntity> findPageStoreWithKeyword(Pageable pageable, @Param("search") String search);

    Optional<StoreEntity> findOneByGuid(UUID storeGuid);

    Optional<StoreEntity> findOneByStoreCode(String storeCode);

}
