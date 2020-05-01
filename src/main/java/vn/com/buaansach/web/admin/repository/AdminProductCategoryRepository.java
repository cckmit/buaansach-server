package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.entity.common.ProductCategoryEntity;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.store.SeatEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    void deleteByProductGuid(UUID productGuid);

    void deleteByCategoryGuid(UUID categoryGuid);
}
