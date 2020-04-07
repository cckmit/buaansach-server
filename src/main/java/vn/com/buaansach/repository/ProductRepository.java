package vn.com.buaansach.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.model.entity.ProductEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findOneByGuid(UUID productGuid);

    Optional<ProductEntity> findOneByProductCode(String productCode);

    @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:search% OR p.productCode LIKE %:search%")
    Page<ProductEntity> findPageProductWithKeyword(Pageable pageable, @Param("search") String search);
}
