package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.ProductEntity;

@Repository
public interface PosProductRepository extends JpaRepository<ProductEntity, Long> {
}
