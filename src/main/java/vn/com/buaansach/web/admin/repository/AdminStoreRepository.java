package vn.com.buaansach.web.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminStoreRepository extends JpaRepository<StoreEntity, Long> {
    @Query("SELECT s FROM StoreEntity s WHERE s.storeName LIKE %:search% OR s.storeCode LIKE %:search%")
    Page<StoreEntity> findPageStoreWithKeyword(Pageable pageable, @Param("search") String search);

    Optional<StoreEntity> findOneByGuid(UUID storeGuid);

    Optional<StoreEntity> findOneByStoreCode(String storeCode);

    @Query(value = "SELECT s.id FROM bas_store s ORDER BY s.id DESC LIMIT 1", nativeQuery = true)
    Long findLastStoreId();
}
