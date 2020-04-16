package vn.com.buaansach.web.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.ProductEntity;

public interface CustomerProductRepository extends JpaRepository<ProductEntity, Long> {

}
