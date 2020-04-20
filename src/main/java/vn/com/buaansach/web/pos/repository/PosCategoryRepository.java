package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.CategoryEntity;

@Repository
public interface PosCategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
