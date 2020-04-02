package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.SeatEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    Optional<SeatEntity> findOneByGuid(UUID guid);

    List<SeatEntity> findByAreaId(Long areaId);

    void deleteByAreaId(Long areaId);

    void deleteByGuid(UUID guid);
}
