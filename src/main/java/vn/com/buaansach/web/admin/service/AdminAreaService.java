package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.notification.StoreOrderNotificationEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.*;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminAreaDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateAreaDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAreaService {
    private final AdminAreaRepository adminAreaRepository;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminSeatRepository adminSeatRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderProductRepository adminOrderProductRepository;
    private final AdminPaymentRepository adminPaymentRepository;
    private final AdminStoreOrderRepository adminStoreOrderRepository;

    /* used when create area with init seats */
    private List<SeatEntity> createListSeat(AreaEntity areaEntity, int numberOfSeats, String seatPrefix) {
        List<SeatEntity> listSeat = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            SeatEntity seatEntity = new SeatEntity();
            seatEntity.setGuid(UUID.randomUUID());
            if (seatPrefix != null && !seatPrefix.isEmpty()) {
                seatEntity.setSeatName(seatPrefix.trim() + " " + i);
            } else {
                seatEntity.setSeatName(String.valueOf(i));
            }
            seatEntity.setSeatStatus(SeatStatus.EMPTY);
            seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
            seatEntity.setSeatLocked(false);
            seatEntity.setOrderGuid(null);
            seatEntity.setAreaGuid(areaEntity.getGuid());
            listSeat.add(seatEntity);
        }
        return adminSeatRepository.saveAll(listSeat);
    }

    @Transactional
    public AdminAreaDTO createArea(AdminCreateAreaDTO request) {
        adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new NotFoundException("admin@storeNotFound@" + request.getStoreGuid()));

        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setGuid(UUID.randomUUID());
        areaEntity.setAreaName(request.getAreaName());
        areaEntity.setAreaType(request.getAreaType());
        areaEntity.setAreaColor(request.getAreaColor());
        areaEntity.setStoreGuid(request.getStoreGuid());
        areaEntity.setAreaActivated(true);
        areaEntity = adminAreaRepository.save(areaEntity);

        List<SeatEntity> listSeat = new ArrayList<>();
        if (request.getNumberOfSeats() > 0) {
            listSeat = createListSeat(areaEntity, request.getNumberOfSeats(), request.getSeatPrefix());
        }

        return new AdminAreaDTO(areaEntity, listSeat);
    }

    public List<AdminAreaDTO> getListAreaByStore(String storeGuid) {
        adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException("admin@storeNotFound@" + storeGuid));

        List<AreaEntity> listArea = adminAreaRepository.findByStoreGuid(UUID.fromString(storeGuid));
        List<SeatEntity> listSeat = adminSeatRepository.findListSeatByStoreGuid(UUID.fromString(storeGuid));
        List<AdminAreaDTO> result = new ArrayList<>();
        listArea.forEach(area -> {
            AdminAreaDTO dto = new AdminAreaDTO(area, listSeat.stream()
                    .filter(seat -> seat.getAreaGuid().equals(area.getGuid()))
                    .collect(Collectors.toList())
            );
            result.add(dto);
        });
        return result;
    }

    public AdminAreaDTO updateArea(AdminAreaDTO updateEntity) {
        AreaEntity currentEntity = adminAreaRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new NotFoundException("admin@areaNotFound@" + updateEntity.getGuid()));

        currentEntity.setAreaName(updateEntity.getAreaName());
        currentEntity.setAreaColor(updateEntity.getAreaColor());
        return new AdminAreaDTO(adminAreaRepository.save(currentEntity), updateEntity.getListSeat());
    }

    @Transactional
    public void deleteArea(String areaGuid) {
        AreaEntity areaEntity = adminAreaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new NotFoundException("admin@areaNotFound@" + areaGuid));

        List<SeatEntity> listSeat = adminSeatRepository.findByAreaGuid(areaEntity.getGuid());

        List<UUID> listSeatGuid = listSeat.stream().map(SeatEntity::getGuid).collect(Collectors.toList());
        List<OrderEntity> listOrder = adminOrderRepository.findBySeatGuidIn(listSeatGuid);

        List<UUID> listOrderGuid = listOrder.stream().map(OrderEntity::getGuid).collect(Collectors.toList());
        List<PaymentEntity> listPayment = adminPaymentRepository.findByOrderGuidIn(listOrderGuid);
        List<OrderProductEntity> listOrderProduct = adminOrderProductRepository.findByOrderGuidIn(listOrderGuid);
        List<StoreOrderNotificationEntity> listStoreOrder = adminStoreOrderRepository.findByAreaGuid(UUID.fromString(areaGuid));

        /* delete all orders, order products, payments related to all seat of area */
        adminPaymentRepository.deleteInBatch(listPayment);
        adminOrderProductRepository.deleteInBatch(listOrderProduct);
        adminOrderRepository.deleteInBatch(listOrder);
        adminStoreOrderRepository.deleteInBatch(listStoreOrder);

        /* then delete all seat of area */
        adminSeatRepository.deleteInBatch(listSeat);

        /* finally, delete area */
        adminAreaRepository.delete(areaEntity);
    }

    public void toggleArea(String areaGuid) {
        AreaEntity areaEntity = adminAreaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new NotFoundException("admin@areaNotFound@" + areaGuid));
        areaEntity.setAreaActivated(!areaEntity.isAreaActivated());
        adminAreaRepository.save(areaEntity);
    }
}
