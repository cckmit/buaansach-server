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
    @Query("SELECT s FROM StoreEntity s WHERE s.name LIKE %:keyword%")
    Page<StoreEntity> getPageStoreWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    Optional<StoreEntity> findOneByCode(UUID storeCode);

    void deleteByCode(UUID storeCode);

    Optional<StoreEntity> findOneByCustomCode(String customCode);
}
