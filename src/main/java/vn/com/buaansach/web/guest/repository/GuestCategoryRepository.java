package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;

import java.util.List;

@Repository
public interface GuestCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("SELECT ce FROM CategoryEntity ce ORDER BY ce.categoryPosition ASC")
    List<CategoryEntity> findAllCategoryOrderByPosition();
}
