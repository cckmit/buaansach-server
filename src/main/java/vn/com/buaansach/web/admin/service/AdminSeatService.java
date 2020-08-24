package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.order.PaymentEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreOrderEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.*;
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
    private final AdminPaymentRepository adminPaymentRepository;
    private final AdminStoreOrderRepository adminStoreOrderRepository;

    public SeatEntity createSeat(AdminCreateSeatDTO request) {
        adminAreaRepository.findOneByGuid(request.getAreaGuid())
                .orElseThrow(() -> new NotFoundException("admin@areaNotFound@" + request.getAreaGuid()));

        SeatEntity seatEntity = new SeatEntity();
        UUID guid = UUID.randomUUID();
        seatEntity.setGuid(guid);
        seatEntity.setSeatName(request.getSeatName());
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setSeatLocked(false);
        seatEntity.setOrderGuid(null);
        seatEntity.setAreaGuid(request.getAreaGuid());
        return adminSeatRepository.save(seatEntity);
    }

    public SeatEntity updateSeat(SeatEntity updateEntity) {
        SeatEntity currentEntity = adminSeatRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new NotFoundException("admin@seatNotFound@" + updateEntity.getGuid()));
        currentEntity.setSeatName(updateEntity.getSeatName());
        return adminSeatRepository.save(currentEntity);
    }

//    public List<SeatEntity> getListSeatByAreaGuid(String areaGuid) {
//        AreaEntity areaEntity = adminAreaRepository.findOneByGuid(UUID.fromString(areaGuid))
//                .orElseThrow(() -> new ResourceNotFoundException("admin@areaNotFound@" + areaGuid));
//        return adminSeatRepository.findByAreaGuid(areaEntity.getGuid());
//    }
//
//    public List<SeatEntity> getListSeatByStoreGuid(String storeGuid) {
//        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
//                .orElseThrow(() -> new ResourceNotFoundException("admin@storeNotFound@" + storeGuid));
//        return adminSeatRepository.findListSeatByStoreGuid(storeEntity.getGuid());
//    }

    @Transactional
    public void deleteSeat(String seatGuid) {
        SeatEntity seatEntity = adminSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException("admin@seatNotFound@" + seatGuid));

        List<OrderEntity> listOrder = adminOrderRepository.findBySeatGuid(seatEntity.getGuid());

        List<UUID> listOrderGuid = listOrder.stream().map(OrderEntity::getGuid).collect(Collectors.toList());
        List<PaymentEntity> listPayment = adminPaymentRepository.findByOrderGuidIn(listOrderGuid);
        List<OrderProductEntity> listOrderProduct = adminOrderProductRepository.findByOrderGuidIn(listOrderGuid);
        List<StoreOrderEntity> listStoreOrder = adminStoreOrderRepository.findBySeatGuid(UUID.fromString(seatGuid));

        /* delete all orders, order products, payments, store orders related to this seat */
        adminPaymentRepository.deleteInBatch(listPayment);
        adminOrderProductRepository.deleteInBatch(listOrderProduct);
        adminOrderRepository.deleteInBatch(listOrder);
        adminStoreOrderRepository.deleteInBatch(listStoreOrder);

        /* finally, delete seat */
        adminSeatRepository.delete(seatEntity);
    }

}
