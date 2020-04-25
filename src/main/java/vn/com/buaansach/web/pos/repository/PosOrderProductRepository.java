package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosOrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
    List<OrderProductEntity> findByOrderGuid(UUID orderGuid);

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO(orderProduct, product) " +
            "FROM OrderProductEntity orderProduct " +
            "LEFT JOIN vn.com.buaansach.entity.OrderEntity od " +
            "ON orderProduct.orderGuid = od.guid " +
            "LEFT JOIN vn.com.buaansach.entity.ProductEntity product " +
            "ON orderProduct.productGuid = product.guid " +
            "WHERE orderProduct.orderGuid = :orderGuid " +
            "ORDER BY orderProduct.id ASC")
    List<PosOrderProductDTO> findListPosOrderProductDTOByOrderGuid(@Param("orderGuid") UUID orderGuid);
}
