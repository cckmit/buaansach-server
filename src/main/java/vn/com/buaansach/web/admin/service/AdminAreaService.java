package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.order.AdminOrderProductRepository;
import vn.com.buaansach.web.admin.repository.order.AdminOrderRepository;
import vn.com.buaansach.web.admin.repository.store.AdminAreaRepository;
import vn.com.buaansach.web.admin.repository.store.AdminSeatRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminAreaDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateAreaDTO;
import vn.com.buaansach.web.shared.service.SeatIdentityService;

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
    private final AdminSeatService adminSeatService;
    private final SeatIdentityService seatIdentityService;

    /* used when create area with init seats */
    @Transactional
    public List<SeatEntity> createListSeat(AreaEntity areaEntity, int numberOfSeats, String seatPrefix, String seatPrefixEng) {
        List<SeatEntity> listSeat = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            SeatEntity seatEntity = new SeatEntity();
            seatEntity.setGuid(UUID.randomUUID());
            if (seatPrefix != null && !seatPrefix.isEmpty()) {
                seatEntity.setSeatName(seatPrefix.trim() + " " + i);
                seatEntity.setSeatNameEng(seatPrefixEng.trim() + " " + i);
            } else {
                seatEntity.setSeatName(String.valueOf(i));
                seatEntity.setSeatNameEng(String.valueOf(i));
            }
            seatEntity.setSeatStatus(SeatStatus.EMPTY);
            seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
            seatEntity.setSeatLocked(false);
            seatEntity.setOrderGuid(null);
            seatEntity.setAreaGuid(areaEntity.getGuid());
            listSeat.add(seatEntity);
        }
        seatIdentityService.createSeatIdentity(listSeat.stream().map(SeatEntity::getGuid).collect(Collectors.toList()));
        return adminSeatRepository.saveAll(listSeat);
    }

    @Transactional
    public AdminAreaDTO createArea(AdminCreateAreaDTO request) {
        adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setGuid(UUID.randomUUID());
        areaEntity.setAreaName(request.getAreaName());
        areaEntity.setAreaNameEng(request.getAreaNameEng());
        areaEntity.setAreaType(request.getAreaType());
        areaEntity.setAreaColor(request.getAreaColor());
        areaEntity.setAreaActivated(request.isAreaActivated());
        areaEntity.setAreaPosition(request.getAreaPosition());
        areaEntity.setStoreGuid(request.getStoreGuid());
        areaEntity = adminAreaRepository.save(areaEntity);

        List<SeatEntity> listSeat = new ArrayList<>();
        if (request.getNumberOfSeats() > 0) {
            listSeat = createListSeat(areaEntity, request.getNumberOfSeats(), request.getSeatPrefix(), request.getSeatPrefixEng());
        }

        return new AdminAreaDTO(areaEntity, listSeat);
    }

    public List<AdminAreaDTO> getListAreaByStore(String storeGuid) {
        adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        List<AreaEntity> listArea = adminAreaRepository.findByStoreGuidOrderByAreaPositionAsc(UUID.fromString(storeGuid));
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

    public AdminAreaDTO updateArea(AdminAreaDTO payload) {
        AreaEntity currentEntity = adminAreaRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        currentEntity.setAreaName(payload.getAreaName());
        currentEntity.setAreaNameEng(payload.getAreaNameEng());
        currentEntity.setAreaColor(payload.getAreaColor());
        currentEntity.setAreaActivated(payload.isAreaActivated());
        currentEntity.setAreaPosition(payload.getAreaPosition());
        return new AdminAreaDTO(adminAreaRepository.save(currentEntity), payload.getListSeat());
    }

    @Transactional
    public void deleteArea(UUID areaGuid) {
        AreaEntity areaEntity = adminAreaRepository.findOneByGuid(areaGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        adminSeatService.deleteByArea(areaGuid);

        /* finally, delete area */
        adminAreaRepository.delete(areaEntity);
    }

    @Transactional
    public void deleteByStore(UUID storeGuid){
        adminAreaRepository.deleteByStoreGuid(storeGuid);
    }
}
