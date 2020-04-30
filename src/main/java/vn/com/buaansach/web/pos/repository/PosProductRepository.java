package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface PosProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByGuidIn(List<UUID> uuids);
}
