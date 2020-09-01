package vn.com.buaansach.web.guest.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuestOrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
    @Query("SELECT new vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO(orderProduct, product) " +
            "FROM OrderProductEntity orderProduct " +
            "LEFT JOIN vn.com.buaansach.entity.order.OrderEntity od " +
            "ON orderProduct.orderGuid = od.guid " +
            "LEFT JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON orderProduct.productGuid = product.guid " +
            "WHERE orderProduct.orderGuid = :orderGuid " +
            "ORDER BY orderProduct.id ASC")
    List<GuestOrderProductDTO> findListGuestOrderProductDTOByOrderGuid(@Param("orderGuid") UUID orderGuid);
}
