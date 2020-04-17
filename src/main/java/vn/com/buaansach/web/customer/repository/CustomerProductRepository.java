package vn.com.buaansach.web.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.ProductEntity;

@Repository
public interface CustomerProductRepository extends JpaRepository<ProductEntity, Long> {

}
