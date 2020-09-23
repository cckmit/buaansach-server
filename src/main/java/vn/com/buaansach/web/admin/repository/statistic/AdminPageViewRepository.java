package vn.com.buaansach.web.admin.repository.statistic;

import org.springframework.data.jpa.repository.Query;
import vn.com.buaansach.web.shared.repository.statistic.PageViewRepository;

import java.time.Instant;

public interface AdminPageViewRepository extends PageViewRepository {
    @Query(value = "SELECT COUNT(*) FROM (SELECT DISTINCT ip_address, browser_name, device_name, device_os FROM bas_page_view WHERE created_date >= :start AND created_date <= :end) AS result", nativeQuery = true)
    int countUniqueVisitor(Instant start, Instant end);

    @Query(value = "SELECT COUNT(*) FROM bas_page_view WHERE created_date >= :start AND created_date <= :end", nativeQuery = true)
    int countTotalPageView(Instant start, Instant end);
}
