package vn.com.buaansach.web.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.ProductEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByGuidIn(List<UUID> uuids);
}
