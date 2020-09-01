package vn.com.buaansach.core.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findOneByGuid(UUID guid);

    Optional<CategoryEntity> findOneByCategoryName(String categoryName);
}
