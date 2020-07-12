package vn.com.buaansach.web.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.store.StoreOrderEntity;

@Repository
public interface GuestStoreOrderRepository extends JpaRepository<StoreOrderEntity, Long> {
}
