package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.pos.repository.store.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.pos.websocket.dto.PosSocketDTO;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosSeatService {
    private final PosStoreRepository posStoreRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosSocketService posSocketService;

    public List<PosSeatDTO> getListSeatByStoreGuid(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException("pos@storeNotFound@" + storeGuid));
        return posSeatRepository.findListPosSeatDTOByStoreGuid(UUID.fromString(storeGuid));
    }

    public PosSeatDTO getPosSeatDTO(String seatGuid) {
        return posSeatRepository.findPosSeatDTOBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException("pos@seatNotFound@" + seatGuid));
    }

    public void resetSeat(SeatEntity seatEntity) {
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setOrderGuid(null);
        posSeatRepository.save(seatEntity);
    }

    public void resetSeat(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException("pos@seatNotFound@" + seatGuid));
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setOrderGuid(null);
        posSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceFinished(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException("pos@seatNotFound@" + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        posSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceUnfinished(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException("pos@seatNotFound@" + seatGuid));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        posSeatRepository.save(seatEntity);
    }

    public void toggleLock(String seatGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException("pos@storeNotFoundWithSeat@" + seatGuid));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException("pos@seatNotFound@" + seatGuid));
        seatEntity.setSeatLocked(!seatEntity.isSeatLocked());
        posSeatRepository.save(seatEntity);

        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage(WebSocketConstants.POS_LOCK_SEAT);
        dto.setPayload(seatEntity);
        posSocketService.sendMessage(WebSocketConstants.TOPIC_GUEST_PREFIX + seatEntity.getGuid(), dto);
    }
}
