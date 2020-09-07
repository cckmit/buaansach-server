package vn.com.buaansach.web.pos.repository.order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.order.OrderProductRepository;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosOrderProductRepository extends OrderProductRepository {

    @Query("SELECT new vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO(orderProduct, product) " +
            "FROM OrderProductEntity orderProduct " +
            "JOIN vn.com.buaansach.entity.order.OrderEntity od " +
            "ON orderProduct.orderGuid = od.guid " +
            "JOIN vn.com.buaansach.entity.common.ProductEntity product " +
            "ON orderProduct.productGuid = product.guid " +
            "WHERE orderProduct.orderGuid = :orderGuid " +
            "ORDER BY orderProduct.id ASC")
    List<PosOrderProductDTO> findListPosOrderProductDTOByOrderGuid(@Param("orderGuid") UUID orderGuid);

}
