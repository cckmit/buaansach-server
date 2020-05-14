package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.repository.AdminAreaRepository;
import vn.com.buaansach.web.admin.repository.AdminSeatRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateSeatDTO;

import java.util.List;
import java.util.UUID;

@Service
public class AdminSeatService {
    private final AdminSeatRepository adminSeatRepository;
    private final AdminAreaRepository adminAreaRepository;
    private final AdminStoreRepository adminStoreRepository;

    public AdminSeatService(AdminSeatRepository adminSeatRepository, AdminAreaRepository adminAreaRepository, AdminStoreRepository adminStoreRepository) {
        this.adminSeatRepository = adminSeatRepository;
        this.adminAreaRepository = adminAreaRepository;
        this.adminStoreRepository = adminStoreRepository;
    }

    public SeatEntity createSeat(AdminCreateSeatDTO request) {
        adminAreaRepository.findOneByGuid(request.getAreaGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + request.getAreaGuid()));

        SeatEntity seatEntity = new SeatEntity();
        UUID guid = UUID.randomUUID();
        seatEntity.setGuid(guid);
        seatEntity.setSeatName(request.getSeatName());
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setSeatLocked(false);
        seatEntity.setCurrentOrderGuid(null);
        seatEntity.setAreaGuid(request.getAreaGuid());
        return adminSeatRepository.save(seatEntity);
    }

    public SeatEntity updateSeat(SeatEntity updateEntity) {
        SeatEntity currentEntity = adminSeatRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + updateEntity.getGuid()));
        currentEntity.setSeatName(updateEntity.getSeatName());
        return adminSeatRepository.save(currentEntity);
    }

    public List<SeatEntity> getListSeatByAreaGuid(String areaGuid) {
        AreaEntity areaEntity = adminAreaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));
        return adminSeatRepository.findByAreaGuid(areaEntity.getGuid());
    }

    public List<SeatEntity> getListSeatByStoreGuid(String storeGuid) {
        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        return adminSeatRepository.findListSeatByStoreGuid(storeEntity.getGuid());
    }

    public void deleteSeat(String seatGuid) {
        SeatEntity seatEntity = adminSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        adminSeatRepository.delete(seatEntity);
    }

}
