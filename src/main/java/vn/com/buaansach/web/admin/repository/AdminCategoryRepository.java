package vn.com.buaansach.web.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.buaansach.entity.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

public interface AdminCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findOneByCategoryName(String categoryName);

    Optional<CategoryEntity> findOneByGuid(UUID guid);

    @Query("SELECT c FROM CategoryEntity c WHERE c.categoryName LIKE %:search%")
    Page<CategoryEntity> findPageCategoryWithKeyword(Pageable pageable, @Param("search") String search);
}
