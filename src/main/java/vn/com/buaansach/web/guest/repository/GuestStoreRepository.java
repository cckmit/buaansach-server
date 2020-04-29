package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.StoreEntity;

@Repository
public interface GuestStoreRepository extends JpaRepository<StoreEntity, Long> {
}
