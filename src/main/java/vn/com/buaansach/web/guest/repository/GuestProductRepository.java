package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.ProductEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuestProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByGuidIn(List<UUID> uuids);
}
