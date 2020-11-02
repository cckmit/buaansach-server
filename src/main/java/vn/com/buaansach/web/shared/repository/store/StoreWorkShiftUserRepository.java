package vn.com.buaansach.web.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreWorkShiftUserEntity;

import java.util.UUID;

@Repository
public interface StoreWorkShiftUserRepository extends JpaRepository<StoreWorkShiftUserEntity, Long> {

    void deleteByStoreWorkShiftGuid(UUID storeWorkShiftGuid);
}
