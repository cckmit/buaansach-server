package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.List;
import java.util.UUID;

@Service
public class PosSeatService {
    private final PosStoreRepository posStoreRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreSecurity posStoreSecurity;

    public PosSeatService(PosStoreRepository posStoreRepository, PosSeatRepository posSeatRepository, PosStoreSecurity posStoreSecurity) {
        this.posStoreRepository = posStoreRepository;
        this.posSeatRepository = posSeatRepository;
        this.posStoreSecurity = posStoreSecurity;
    }

    public List<PosSeatDTO> getListSeatByStoreGuid(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        return posSeatRepository.findListPosSeatDTOByStoreGuid(UUID.fromString(storeGuid));
    }

    public PosSeatDTO getPosSeatDTO(String seatGuid) {
        return posSeatRepository.findPosSeatDTOBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new BadRequestException("Seat not found with guid: " + seatGuid));
    }

    public void resetSeat(SeatEntity seatEntity) {
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setCurrentOrderGuid(null);
        posSeatRepository.save(seatEntity);
    }

    public void resetSeat(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setCurrentOrderGuid(null);
        posSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceFinished(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        posSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceUnfinished(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        posSeatRepository.save(seatEntity);
    }

    public void toggleLock(String seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        seatEntity.setSeatLocked(!seatEntity.isSeatLocked());
        posSeatRepository.save(seatEntity);
    }
}
