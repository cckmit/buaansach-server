package vn.com.buaansach.core.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.order.OrderProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
    List<OrderProductEntity> findByOrderGuidIn(List<UUID> listOrderGuid);

    List<OrderProductEntity> findByOrderGuidAndOrderProductStatusIn(UUID orderGuid, List<OrderProductStatus> listStatus);

    Optional<OrderProductEntity> findOneByGuid(UUID orderProductGuid);

    List<OrderProductEntity> findByGuidIn(List<UUID> uuids);

    List<OrderProductEntity> findByOrderGuid(UUID orderGuid);

}
