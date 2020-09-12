package vn.com.buaansach.web.guest.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.common.CategoryRepository;
import vn.com.buaansach.entity.common.CategoryEntity;

import java.util.List;

@Repository
public interface GuestCategoryRepository extends CategoryRepository {
    @Query("SELECT ce FROM CategoryEntity ce WHERE ce.categoryActivated = TRUE ORDER BY ce.categoryPosition ASC")
    List<CategoryEntity> findAllActiveCategoryOrderByPositionAsc();
}
