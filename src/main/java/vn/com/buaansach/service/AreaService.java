package vn.com.buaansach.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.AreaRepository;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.service.dto.AreaDTO;
import vn.com.buaansach.service.request.CreateAreaRequest;

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

    public AreaService(AreaRepository areaRepository, StoreRepository storeRepository, SeatService seatService) {
        this.areaRepository = areaRepository;
        this.storeRepository = storeRepository;
        this.seatService = seatService;
    }

    @Transactional
    public AreaDTO createArea(CreateAreaRequest request) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(request.getStoreGuid()))
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
