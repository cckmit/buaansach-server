package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.order.OrderEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findOneByGuid(UUID fromString);

    @Query("SELECT orderEntity FROM OrderEntity orderEntity " +
            "LEFT JOIN vn.com.buaansach.entity.store.SeatEntity seat " +
            "ON orderEntity.guid = seat.currentOrderGuid " +
            "WHERE seat.guid = :seatGuid")
    Optional<OrderEntity> findSeatCurrentOrder(@Param("seatGuid") UUID seatGuid);

    @Query("SELECT orderEntity FROM OrderEntity orderEntity " +
            "WHERE orderEntity.cashierLogin = :userLogin " +
            "AND orderEntity.createdDate >= :startDate " +
            "AND orderEntity.createdDate <= :endDate")
    List<OrderEntity> findListOrderForReportByUser(@Param("userLogin") String userLogin,
                                                   @Param("startDate") Instant startDate,
                                                   @Param("endDate") Instant endDate);

    @Query("SELECT orderEntity FROM OrderEntity orderEntity " +
            "WHERE orderEntity.createdDate >= :startDate " +
            "AND orderEntity.createdDate <= :endDate")
    List<OrderEntity> findListOrderForReport(@Param("startDate") Instant startDate,
                                             @Param("endDate") Instant endDate);
}
