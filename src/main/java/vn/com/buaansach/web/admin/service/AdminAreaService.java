package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.repository.AdminAreaRepository;
import vn.com.buaansach.web.admin.repository.AdminSeatRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.AdminAreaDTO;
import vn.com.buaansach.web.admin.service.manipulation.AdminCreateAreaDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminAreaService {
    private final AdminAreaRepository adminAreaRepository;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminSeatService adminSeatService;
    private final AdminSeatRepository adminSeatRepository;

    public AdminAreaService(AdminAreaRepository adminAreaRepository, AdminStoreRepository adminStoreRepository, AdminSeatService adminSeatService, AdminSeatRepository adminSeatRepository) {
        this.adminAreaRepository = adminAreaRepository;
        this.adminStoreRepository = adminStoreRepository;
        this.adminSeatService = adminSeatService;
        this.adminSeatRepository = adminSeatRepository;
    }


    /* used when create area with init seats */
    private List<SeatEntity> createListSeat(AreaEntity areaEntity, int numberOfSeats, String seatPrefix) {
        List<SeatEntity> listSeat = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            SeatEntity seatEntity = new SeatEntity();
            UUID guid = UUID.randomUUID();
            seatEntity.setGuid(guid);
            if (seatPrefix != null && !seatPrefix.isEmpty()) {
                seatEntity.setSeatName(seatPrefix.trim() + " " + i);
            } else {
                seatEntity.setSeatName(String.valueOf(i));
            }
            seatEntity.setAreaId(areaEntity.getId());
            listSeat.add(seatEntity);
        }
        return adminSeatRepository.saveAll(listSeat);
    }

    @Transactional
    public AdminAreaDTO createArea(AdminCreateAreaDTO request) {
        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + request.getStoreGuid()));

        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setGuid(UUID.randomUUID());
        areaEntity.setAreaName(request.getAreaName());
        areaEntity.setStoreId(storeEntity.getId());
        areaEntity = adminAreaRepository.save(areaEntity);

        List<SeatEntity> listSeat = new ArrayList<>();
        if (request.getNumberOfSeats() > 0) {
            listSeat = createListSeat(areaEntity, request.getNumberOfSeats(), request.getSeatPrefix());
        }

        return new AdminAreaDTO(areaEntity, listSeat);
    }

    public List<AdminAreaDTO> getListAreaByStore(String storeGuid) {
        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        List<AreaEntity> listArea = adminAreaRepository.findByStoreId(storeEntity.getId());
        List<SeatEntity> listSeat = adminSeatRepository.findListSeatByStoreId(storeEntity.getId());
        List<AdminAreaDTO> result = new ArrayList<>();
        listArea.forEach(area -> {
            AdminAreaDTO dto = new AdminAreaDTO(
                    area,
                    listSeat.stream()
                            .filter(seat -> seat.getAreaId().equals(area.getId()))
                            .collect(Collectors.toList())
            );
            result.add(dto);
        });
        return result;
    }

    public AdminAreaDTO updateArea(AreaEntity updateEntity) {
        AreaEntity currentEntity = adminAreaRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + updateEntity.getGuid()));

        currentEntity.setAreaName(updateEntity.getAreaName());
        return new AdminAreaDTO(adminAreaRepository.save(currentEntity), adminSeatService.getListSeatByAreaGuid(currentEntity.getGuid().toString()));
    }

    public void deleteArea(String areaGuid) {
        AreaEntity areaEntity = adminAreaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));

        adminSeatService.deleteAllSeatByAreaId(areaEntity.getId());
        adminAreaRepository.delete(areaEntity);
    }

    public void deleteAllAreaByStoreId(Long storeId) {
        List<AreaEntity> listArea = adminAreaRepository.findByStoreId(storeId);
        listArea.forEach(areaEntity -> {
            adminSeatService.deleteAllSeatByAreaId(areaEntity.getId());
        });
        adminAreaRepository.deleteAll(listArea);
    }
}
