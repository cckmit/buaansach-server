package vn.com.buaansach.web.pos.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.shared.repository.common.CategoryRepository;
import vn.com.buaansach.entity.common.CategoryEntity;

import java.util.List;

@Repository
public interface PosCategoryRepository extends CategoryRepository {
    @Query("SELECT ce FROM CategoryEntity ce ORDER BY ce.categoryPosition ASC")
    List<CategoryEntity> findAllCategoryOrderByPositionAsc();
}
