package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.service.dto.AdminAreaDTO;
import vn.com.buaansach.web.common.service.dto.manipulation.CreateAreaDTO;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.repository.AreaRepository;
import vn.com.buaansach.repository.StoreRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminAreaService {
    private final AreaRepository areaRepository;
    private final StoreRepository storeRepository;
    private final AdminSeatService adminSeatService;

    public AdminAreaService(AreaRepository areaRepository, StoreRepository storeRepository, AdminSeatService adminSeatService) {
        this.areaRepository = areaRepository;
        this.storeRepository = storeRepository;
        this.adminSeatService = adminSeatService;
    }

    @Transactional
    public AdminAreaDTO createArea(CreateAreaDTO request) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + request.getStoreGuid()));

        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setGuid(UUID.randomUUID());
        areaEntity.setAreaName(request.getAreaName());
        areaEntity.setStoreId(storeEntity.getId());
        areaEntity = areaRepository.save(areaEntity);

        List<SeatEntity> listSeat = new ArrayList<>();
        if (request.getNumberOfSeats() > 0) {
            listSeat = adminSeatService.createListSeat(areaEntity, request.getNumberOfSeats(), request.getSeatPrefix());
        }

        return new AdminAreaDTO(areaEntity, listSeat);
    }

    public List<AdminAreaDTO> getListAreaByStore(String storeGuid) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        List<AreaEntity> listArea = areaRepository.findByStoreId(storeEntity.getId());
        List<SeatEntity> listSeat = adminSeatService.getListSeatByStoreId(storeEntity.getId());
        List<AdminAreaDTO> result = new ArrayList<>();
        listArea.forEach(area -> {
            AdminAreaDTO dto = new AdminAreaDTO(
                    area,
                    listSeat
                            .stream()
                            .filter(seat -> seat.getAreaId().equals(area.getId()))
                            .collect(Collectors.toList())
            );
            result.add(dto);
        });
        return result;
    }

    public AdminAreaDTO updateArea(AreaEntity updateEntity) {
        AreaEntity currentEntity = areaRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + updateEntity.getGuid()));

        currentEntity.setAreaName(updateEntity.getAreaName());
        return new AdminAreaDTO(areaRepository.save(currentEntity), adminSeatService.getListSeatByAreaGuid(currentEntity.getGuid().toString()));
    }

    public void deleteArea(String areaGuid) {
        AreaEntity areaEntity = areaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));

        adminSeatService.deleteAllSeatByAreaId(areaEntity.getId());
        areaRepository.delete(areaEntity);
    }

    public void deleteAllAreaByStoreId(Long storeId) {
        List<AreaEntity> listArea = areaRepository.findByStoreId(storeId);
        listArea.forEach(areaEntity -> {
            adminSeatService.deleteAllSeatByAreaId(areaEntity.getId());
        });
        areaRepository.deleteAll(listArea);
    }
}
