package vn.com.buaansach.web.admin.repository.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.web.shared.repository.store.StoreRepository;

@Repository
public interface AdminStoreRepository extends StoreRepository {
    @Query("SELECT s FROM StoreEntity s WHERE s.storeName LIKE %:search% OR s.storeCode LIKE %:search%")
    Page<StoreEntity> findPageStoreWithKeyword(Pageable pageable, @Param("search") String search);
}
