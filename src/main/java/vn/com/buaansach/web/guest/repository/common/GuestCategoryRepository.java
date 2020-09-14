package vn.com.buaansach.web.guest.repository.common;

import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;
import vn.com.buaansach.web.shared.repository.common.CategoryRepository;

import java.util.List;

@Repository
public interface GuestCategoryRepository extends CategoryRepository {
    List<CategoryEntity> findByCategoryHiddenFalseOrderByCategoryPositionAsc();
}
