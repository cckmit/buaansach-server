package vn.com.buaansach.web.shared.repository.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.entity.statistic.PageViewEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PageViewRepository extends JpaRepository<PageViewEntity, Long> {

}
