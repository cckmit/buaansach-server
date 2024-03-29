package vn.com.buaansach.web.pos.repository.order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.web.shared.repository.order.OrderRepository;
import vn.com.buaansach.entity.order.OrderEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosOrderRepository extends OrderRepository {

    @Query("SELECT orderEntity FROM OrderEntity orderEntity " +
            "JOIN vn.com.buaansach.entity.store.SeatEntity seat " +
            "ON orderEntity.guid = seat.orderGuid " +
            "WHERE seat.guid = :seatGuid")
    Optional<OrderEntity> findSeatCurrentOrder(@Param("seatGuid") UUID seatGuid);

    @Query("SELECT orderEntity FROM OrderEntity orderEntity " +
            "LEFT JOIN vn.com.buaansach.entity.store.SeatEntity seat " +
            "ON orderEntity.seatGuid = seat.guid " +
            "LEFT JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "WHERE orderEntity.orderReceivedBy = :userLogin " +
            "AND orderEntity.createdDate >= :startDate " +
            "AND orderEntity.createdDate <= :endDate " +
            "AND area.storeGuid = :storeGuid")
    List<OrderEntity> findListOrderForReportByUser(@Param("userLogin") String userLogin,
                                                   @Param("startDate") Instant startDate,
                                                   @Param("endDate") Instant endDate,
                                                   @Param("storeGuid") UUID storeGuid);

    @Query("SELECT orderEntity FROM OrderEntity orderEntity " +
            "LEFT JOIN vn.com.buaansach.entity.store.SeatEntity seat " +
            "ON orderEntity.seatGuid = seat.guid " +
            "LEFT JOIN vn.com.buaansach.entity.store.AreaEntity area " +
            "ON seat.areaGuid = area.guid " +
            "WHERE orderEntity.createdDate >= :startDate " +
            "AND orderEntity.createdDate <= :endDate " +
            "AND area.storeGuid = :storeGuid")
    List<OrderEntity> findListOrderForReport(@Param("startDate") Instant startDate,
                                             @Param("endDate") Instant endDate,
                                             @Param("storeGuid") UUID storeGuid);
}
