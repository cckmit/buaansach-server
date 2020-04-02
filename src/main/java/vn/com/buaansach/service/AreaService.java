package vn.com.buaansach.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.AreaRepository;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.service.request.CreateAreaRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class AreaService {
    private final AreaRepository areaRepository;
    private final StoreRepository storeRepository;
    private final SeatService seatService;
    private final FileService fileService;

    public AreaService(AreaRepository areaRepository, StoreRepository storeRepository, SeatService seatService, FileService fileService) {
        this.areaRepository = areaRepository;
        this.storeRepository = storeRepository;
        this.seatService = seatService;
        this.fileService = fileService;
    }

    @Transactional
    public AreaEntity createArea(CreateAreaRequest request) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(request.getStoreGuid()))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + request.getStoreGuid()));

        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setGuid(UUID.randomUUID());
        areaEntity.setAreaName(request.getAreaName());
        areaEntity.setStoreId(storeEntity.getId());
        areaEntity = areaRepository.save(areaEntity);

        if (request.getNumberOfSeats() > 0) {
            seatService.createListSeat(areaEntity, request.getNumberOfSeats());
        }
        return areaEntity;
    }

    public AreaEntity updateArea(AreaEntity updateEntity) {
        AreaEntity currentEntity = areaRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + updateEntity.getGuid()));
        currentEntity.setAreaName(updateEntity.getAreaName());
        return areaRepository.save(currentEntity);
    }

    public List<AreaEntity> getListAreaByStore(String storeGuid) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        return areaRepository.findByStoreId(storeEntity.getId());
    }

    public void deleteArea(String areaGuid) {
        AreaEntity areaEntity = areaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));
        seatService.getListSeatByArea(areaGuid).forEach(seatEntity -> {
            fileService.deleteByUrl(seatEntity.getSeatQrCode());
        });
        seatService.deleteByAreaId(areaEntity.getId());
        areaRepository.delete(areaEntity);
    }

    public void deleteAreaByStoreId(Long storeId){
        List<AreaEntity> listArea = areaRepository.findByStoreId(storeId);
        listArea.forEach(areaEntity -> {
            seatService.deleteByAreaId(areaEntity.getId());
        });
        areaRepository.deleteAll(listArea);
    }
}
