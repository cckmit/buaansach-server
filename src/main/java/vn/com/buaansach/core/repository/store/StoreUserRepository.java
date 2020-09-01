package vn.com.buaansach.core.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreUserRepository extends JpaRepository<StoreUserEntity, Long> {
    Optional<StoreUserEntity> findOneByUserLoginAndStoreGuid(String userLogin, UUID storeGuid);

    Optional<StoreUserEntity> findOneByGuid(UUID guid);

    void deleteByStoreGuid(UUID storeGuid);

    List<StoreUserEntity> findByStoreGuid(UUID storeGuid);

    Optional<StoreUserEntity> findByStoreGuidAndUserLogin(UUID storeGuid, String userLogin);

}
