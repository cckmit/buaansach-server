package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.pos.repository.store.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosToggleLockListSeatDTO;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.pos.websocket.dto.PosSocketDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosSeatService {
    private final PosStoreRepository posStoreRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosSocketService posSocketService;

    public PosSeatDTO getPosSeatDTO(String seatGuid) {
        return posSeatRepository.findPosSeatDTOBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
    }

    /* Must have an ID */
    public void resetSeat(SeatEntity seatEntity) {
        seatEntity.setSeatLocked(false);
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setOrderGuid(null);
        posSeatRepository.save(seatEntity);
    }

    public void resetListSeat(List<SeatEntity> listSeat){
        listSeat.forEach(seatEntity -> {
            seatEntity.setSeatLocked(false);
            seatEntity.setSeatStatus(SeatStatus.EMPTY);
            seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
            seatEntity.setOrderGuid(null);
        });
        posSeatRepository.saveAll(listSeat);
    }

    public void resetSeat(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        seatEntity.setSeatLocked(false);
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setOrderGuid(null);
        posSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceFinished(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        posSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceUnfinished(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        posSeatRepository.save(seatEntity);
    }

    public void makeSeatServiceUnfinished(SeatEntity seatEntity) {
        seatEntity.setSeatServiceStatus(SeatServiceStatus.UNFINISHED);
        posSeatRepository.save(seatEntity);
    }

    public void toggleLock(String seatGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        SeatEntity seatEntity = posSeatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        seatEntity.setSeatLocked(!seatEntity.isSeatLocked());
        posSeatRepository.save(seatEntity);

        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage(WebSocketConstants.POS_LOCK_SEAT);
        dto.setPayload(seatEntity);
        posSocketService.sendMessage(WebSocketConstants.TOPIC_GUEST_PREFIX + seatEntity.getGuid(), dto);
    }

    public void toggleLockListSeat(PosToggleLockListSeatDTO payload){
        List<SeatEntity> list = posSeatRepository.findByGuidIn(payload.getListSeatGuid());
        if (list.size() != payload.getListSeatGuid().size())
            throw new BadRequestException(ErrorCode.LIST_SEAT_SIZE_NOT_MATCH);
        list = list.stream().peek(item -> {
            item.setSeatLocked(payload.isLocked());
        }).collect(Collectors.toList());
        posSeatRepository.saveAll(list);

        list.forEach(item -> {
            PosSocketDTO dto = new PosSocketDTO();
            dto.setMessage(WebSocketConstants.POS_LOCK_SEAT);
            dto.setPayload(item);
            posSocketService.sendMessage(WebSocketConstants.TOPIC_GUEST_PREFIX + item.getGuid(), dto);
        });
    }
}
