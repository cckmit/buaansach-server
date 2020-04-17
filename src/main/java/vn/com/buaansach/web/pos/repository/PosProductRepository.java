package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.ProductEntity;

public interface PosProductRepository extends JpaRepository<ProductEntity, Long> {
}
