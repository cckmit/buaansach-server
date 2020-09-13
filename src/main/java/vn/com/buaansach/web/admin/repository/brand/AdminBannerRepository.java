package vn.com.buaansach.web.admin.repository.brand;

import org.springframework.data.jpa.repository.Query;
import vn.com.buaansach.web.shared.repository.brand.BannerRepository;

public interface AdminBannerRepository extends BannerRepository {
    @Query("SELECT MAX(be.bannerOrder) FROM BannerEntity be")
    Integer findLastBannerOrder();
}
