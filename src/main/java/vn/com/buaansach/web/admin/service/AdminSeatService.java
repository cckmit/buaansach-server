package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.order.AdminOrderProductRepository;
import vn.com.buaansach.web.admin.repository.order.AdminOrderRepository;
import vn.com.buaansach.web.admin.repository.store.AdminAreaRepository;
import vn.com.buaansach.web.admin.repository.store.AdminSeatRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateSeatDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSeatService {
    private final AdminSeatRepository adminSeatRepository;
    private final AdminAreaRepository adminAreaRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderProductRepository adminOrderProductRepository;

    public SeatEntity createSeat(AdminCreateSeatDTO request) {
        adminAreaRepository.findOneByGuid(request.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        SeatEntity seatEntity = new SeatEntity();
        UUID guid = UUID.randomUUID();
        seatEntity.setGuid(guid);
        seatEntity.setSeatName(request.getSeatName());
        seatEntity.setSeatNameEng(request.getSeatNameEng());
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setSeatLocked(false);
        seatEntity.setOrderGuid(null);
        seatEntity.setAreaGuid(request.getAreaGuid());
        return adminSeatRepository.save(seatEntity);
    }

    public SeatEntity updateSeat(SeatEntity updateEntity) {
        SeatEntity currentEntity = adminSeatRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        currentEntity.setSeatName(updateEntity.getSeatName());
        currentEntity.setSeatNameEng(updateEntity.getSeatNameEng());
        return adminSeatRepository.save(currentEntity);
    }

    @Transactional
    public void deleteSeat(String seatGuid) {
        SeatEntity seatEntity = adminSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));

        List<OrderEntity> listOrder = adminOrderRepository.findBySeatGuid(seatEntity.getGuid());

        List<UUID> listOrderGuid = listOrder.stream().map(OrderEntity::getGuid).collect(Collectors.toList());
        List<OrderProductEntity> listOrderProduct = adminOrderProductRepository.findByOrderGuidIn(listOrderGuid);

        /* delete all orders, order products related to this seat */
        adminOrderProductRepository.deleteInBatch(listOrderProduct);
        adminOrderRepository.deleteInBatch(listOrder);

        /* finally, delete seat */
        adminSeatRepository.delete(seatEntity);
    }

}
