package vn.com.buaansach.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.model.entity.AreaEntity;
import vn.com.buaansach.model.entity.SeatEntity;
import vn.com.buaansach.model.entity.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.AreaRepository;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.model.dto.AreaDTO;
import vn.com.buaansach.model.dto.request.CreateAreaRequest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AreaService {
    private final AreaRepository areaRepository;
    private final StoreRepository storeRepository;
    private final SeatService seatService;
    private final StoreUserSecurityService storeUserSecurityService;

    public AreaService(AreaRepository areaRepository, StoreRepository storeRepository, SeatService seatService, StoreUserSecurityService storeUserSecurityService) {
        this.areaRepository = areaRepository;
        this.storeRepository = storeRepository;
        this.seatService = seatService;
        this.storeUserSecurityService = storeUserSecurityService;
    }

    @Transactional
    public AreaDTO createArea(CreateAreaRequest request) {
        storeUserSecurityService.blockAccessIfNotOwnerOrManager(request.getStoreGuid());

        StoreEntity storeEntity = storeRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + request.getStoreGuid()));

        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setGuid(UUID.randomUUID());
        areaEntity.setAreaName(request.getAreaName());
        areaEntity.setStoreId(storeEntity.getId());
        areaEntity = areaRepository.save(areaEntity);

        List<SeatEntity> listSeat = new ArrayList<>();
        if (request.getNumberOfSeats() > 0) {
            listSeat = seatService.createListSeat(areaEntity, request.getNumberOfSeats(), request.getSeatPrefix());
        }

        return new AreaDTO(areaEntity, listSeat);
    }

    public AreaDTO updateArea(AreaEntity updateEntity) {
        AreaEntity currentEntity = areaRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + updateEntity.getGuid()));

        StoreEntity storeEntity = storeRepository.findById(currentEntity.getStoreId())
                .orElseThrow(()-> new ResourceNotFoundException("store", "id", currentEntity.getStoreId()));
        storeUserSecurityService.blockAccessIfNotOwnerOrManager(storeEntity.getGuid());

        currentEntity.setAreaName(updateEntity.getAreaName());
        return new AreaDTO(areaRepository.save(currentEntity), seatService.getListSeatByAreaGuid(currentEntity.getGuid().toString()));
    }

    public List<AreaDTO> getListAreaByStore(String storeGuid) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        List<AreaEntity> listArea = areaRepository.findByStoreId(storeEntity.getId());
        List<SeatEntity> listSeat = seatService.getListSeatByStoreId(storeEntity.getId());
        List<AreaDTO> result = new ArrayList<>();
        listArea.forEach(area -> {
            AreaDTO dto = new AreaDTO(
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

    public void deleteArea(String areaGuid) {
        AreaEntity areaEntity = areaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));

        StoreEntity storeEntity = storeRepository.findById(areaEntity.getStoreId())
                .orElseThrow(()-> new ResourceNotFoundException("store", "id", areaEntity.getStoreId()));
        storeUserSecurityService.blockAccessIfNotOwnerOrManager(storeEntity.getGuid());

        seatService.deleteByAreaId(areaEntity.getId());
        areaRepository.delete(areaEntity);
    }

    public void deleteAreaByStoreId(Long storeId) {
        List<AreaEntity> listArea = areaRepository.findByStoreId(storeId);
        listArea.forEach(areaEntity -> {
            seatService.deleteByAreaId(areaEntity.getId());
        });
        areaRepository.deleteAll(listArea);
    }
}
