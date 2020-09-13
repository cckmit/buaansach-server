package vn.com.buaansach.web.shared.repository.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.brand.BannerEntity;
import vn.com.buaansach.entity.sale.SaleEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long> {

    Optional<BannerEntity> findOneByGuid(UUID guid);

    void deleteByGuid(UUID saleGuid);
}
