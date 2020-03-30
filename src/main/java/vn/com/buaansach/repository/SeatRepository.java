package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.SeatEntity;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
}
