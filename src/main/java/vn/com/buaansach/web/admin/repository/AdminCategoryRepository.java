package vn.com.buaansach.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findOneByCategoryName(String categoryName);

    Optional<CategoryEntity> findOneByGuid(UUID guid);
}
