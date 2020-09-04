package vn.com.buaansach.shared.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findOneByGuid(UUID productGuid);

    Optional<ProductEntity> findOneByProductCode(String productCode);

    List<ProductEntity> findByGuidIn(List<UUID> uuids);

}
