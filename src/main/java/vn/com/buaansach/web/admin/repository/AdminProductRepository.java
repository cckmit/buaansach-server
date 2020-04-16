package vn.com.buaansach.web.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.web.admin.service.dto.AdminProductDTO;

import java.util.Optional;
import java.util.UUID;

public interface AdminProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findOneByGuid(UUID productGuid);

    Optional<ProductEntity> findOneByProductCode(String productCode);

    @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:search% OR p.productCode LIKE %:search%")
    Page<ProductEntity> findPageProductWithKeyword(Pageable pageable, @Param("search") String search);

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.AdminProductDTO(p, c) FROM ProductEntity p " +
            "LEFT JOIN vn.com.buaansach.entity.CategoryEntity c ON p.categoryId = c.id " +
            "WHERE p.productName LIKE %:search% OR p.productCode LIKE %:search%")
    Page<AdminProductDTO> findPageDtoWithKeyword(Pageable pageable, @Param("search") String search);

    @Query("SELECT new vn.com.buaansach.web.admin.service.dto.AdminProductDTO(p, c) FROM ProductEntity p " +
            "LEFT JOIN vn.com.buaansach.entity.CategoryEntity c ON p.categoryId = c.id " +
            "WHERE p.guid = :productGuid")
    Optional<AdminProductDTO> findOneDtoByGuid(@Param("productGuid") UUID productGuid);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.categoryId = NULL WHERE p.categoryId = :categoryId")
    void clearProductCategory(@Param("categoryId") Long categoryId);
}
