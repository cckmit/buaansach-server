package vn.com.buaansach.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.entity.StoreEntity;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    @Query("SELECT s FROM StoreEntity s WHERE s.name LIKE %:search% OR s.code LIKE %:search%")
    Page<StoreEntity> getPageStoreWithKeyword(Pageable pageable, @Param("search") String search);

    Optional<StoreEntity> findOneById(Long id);

    Optional<StoreEntity> findOneByCode(String storeCode);

}
