package vn.com.buaansach.web.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.OrderEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PosOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findOneByGuid(UUID fromString);

    @Query("SELECT orderEntity FROM OrderEntity orderEntity " +
            "LEFT JOIN vn.com.buaansach.entity.SeatEntity seat " +
            "ON orderEntity.guid = seat.currentOrderGuid " +
            "WHERE seat.guid = :seatGuid")
    Optional<OrderEntity> findOneBySeatGuid(@Param("seatGuid") UUID seatGuid);
}
