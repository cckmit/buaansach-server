package vn.com.buaansach.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.AreaRepository;
import vn.com.buaansach.repository.SeatRepository;
import vn.com.buaansach.repository.StoreRepository;
import vn.com.buaansach.service.request.CreateSeatRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final AreaRepository areaRepository;
    private final StoreRepository storeRepository;

    public SeatService(SeatRepository seatRepository, AreaRepository areaRepository, StoreRepository storeRepository) {
        this.seatRepository = seatRepository;
        this.areaRepository = areaRepository;
        this.storeRepository = storeRepository;
    }

    /* used when create area with init seats */
    public List<SeatEntity> createListSeat(AreaEntity areaEntity, int numberOfSeats, String seatPrefix) {
        List<SeatEntity> listSeat = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            SeatEntity seatEntity = new SeatEntity();
            UUID guid = UUID.randomUUID();
            seatEntity.setGuid(guid);
            if (seatPrefix != null && !seatPrefix.isEmpty()){
            seatEntity.setSeatName(seatPrefix.trim() + " " + i);
            } else {
                seatEntity.setSeatName(String.valueOf(i));
            }
            seatEntity.setAreaId(areaEntity.getId());
            listSeat.add(seatEntity);
        }
        return seatRepository.saveAll(listSeat);
    }

    public SeatEntity createSeat(CreateSeatRequest request) {
        AreaEntity areaEntity = areaRepository.findOneByGuid(UUID.fromString(request.getAreaGuid()))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + request.getAreaGuid()));

        SeatEntity seatEntity = new SeatEntity();
        UUID guid = UUID.randomUUID();
        seatEntity.setGuid(guid);
        seatEntity.setSeatName(request.getSeatName());
        seatEntity.setAreaId(areaEntity.getId());
        return seatRepository.save(seatEntity);
    }

    public SeatEntity updateSeat(SeatEntity updateEntity) {
        SeatEntity currentEntity = seatRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + updateEntity.getGuid()));
        currentEntity.setSeatName(updateEntity.getSeatName());
        return seatRepository.save(currentEntity);
    }

    public List<SeatEntity> getListSeatByAreaGuid(String areaGuid) {
        AreaEntity areaEntity = areaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));
        return seatRepository.findByAreaId(areaEntity.getId());
    }

    public void deleteSeat(String seatGuid) {
        SeatEntity seatEntity = seatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatRepository.delete(seatEntity);
    }

    public void deleteByAreaId(Long areaId) {
        List<SeatEntity> listSeat = seatRepository.findByAreaId(areaId);
        seatRepository.deleteAll(listSeat);
    }

    public List<SeatEntity> getListSeatByStoreId(Long storeId) {
        return seatRepository.findSeatEntitiesByStoreId(storeId);
    }

    public List<SeatEntity> getListSeatByStoreGuid(String storeGuid) {
        StoreEntity storeEntity = storeRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        return getListSeatByStoreId(storeEntity.getId());
    }
}
