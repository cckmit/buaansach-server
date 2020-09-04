package vn.com.buaansach.shared.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProductEntity, Long> {

    Optional<StoreProductEntity> findOneByGuid(UUID storeProductGuid);

    List<StoreProductEntity> findByProductGuid(UUID productGuid);

    void deleteByStoreGuid(UUID guid);

    List<StoreProductEntity> findByStoreGuidAndProductGuidIn(UUID storeGuid, List<UUID> uuids);
}
