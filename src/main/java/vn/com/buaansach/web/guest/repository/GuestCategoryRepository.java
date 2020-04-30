package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.CategoryEntity;

@Repository
public interface GuestCategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
