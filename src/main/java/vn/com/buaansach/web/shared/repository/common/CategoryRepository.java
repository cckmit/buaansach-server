package vn.com.buaansach.web.shared.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findOneByGuid(UUID guid);
    Optional<CategoryEntity> findOneByCategoryName(String categoryName);
    Optional<CategoryEntity> findOneByCategoryNameEng(String categoryNameEng);
    List<CategoryEntity> findAllByOrderByCategoryPositionAsc();
}
