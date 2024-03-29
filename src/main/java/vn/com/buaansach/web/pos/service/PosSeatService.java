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
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.util.WebSocketMessages;
import vn.com.buaansach.web.pos.repository.store.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosToggleLockListSeatDTO;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.shared.service.SeatIdentityService;
import vn.com.buaansach.web.shared.websocket.dto.DataSocketDTO;

import javax.transaction.Transactional;
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
    private final SeatIdentityService seatIdentityService;

    public PosSeatDTO getPosSeatDTO(String seatGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        return posSeatRepository.findPosSeatDTOBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
    }

    /* Must have an ID */
    @Transactional
    public void resetSeat(SeatEntity seatEntity) {
        seatEntity.setSeatLocked(false);
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setOrderGuid(null);
        posSeatRepository.save(seatEntity);
        seatIdentityService.resetSeatIdentity(seatEntity.getGuid());
    }

    @Transactional
    public void resetListSeat(List<SeatEntity> listSeat) {
        listSeat.forEach(seatEntity -> {
            seatEntity.setSeatLocked(false);
            seatEntity.setSeatStatus(SeatStatus.EMPTY);
            seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
            seatEntity.setOrderGuid(null);
        });
        posSeatRepository.saveAll(listSeat);
        seatIdentityService.resetSeatIdentity(listSeat.stream()
                .map(SeatEntity::getGuid)
                .collect(Collectors.toList()));
    }

    public void resetSeat(UUID seatGuid) {
        SeatEntity seatEntity = posSeatRepository.findOneByGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        seatEntity.setSeatLocked(false);
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setOrderGuid(null);
        posSeatRepository.save(seatEntity);
        seatIdentityService.resetSeatIdentity(seatGuid);
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

        DataSocketDTO dto = new DataSocketDTO();
        dto.setMessage(WebSocketMessages.POS_LOCK_SEAT);
        dto.setPayload(seatEntity);
        posSocketService.sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + seatEntity.getGuid(), dto);
    }

    public void toggleLockListSeat(PosToggleLockListSeatDTO payload) {
        List<SeatEntity> listSeat = posSeatRepository.findByGuidIn(payload.getListSeatGuid());
        if (listSeat.size() != payload.getListSeatGuid().size())
            throw new BadRequestException(ErrorCode.SOME_SEAT_NOT_FOUND);
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(listSeat.get(0).getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        listSeat = listSeat.stream().peek(item -> {
            item.setSeatLocked(payload.isLocked());
        }).collect(Collectors.toList());
        posSeatRepository.saveAll(listSeat);

        listSeat.forEach(item -> {
            DataSocketDTO dto = new DataSocketDTO();
            dto.setMessage(WebSocketMessages.POS_LOCK_SEAT);
            dto.setPayload(item);
            posSocketService.sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + item.getGuid(), dto);
        });
    }
}
