package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.SeatIdentityEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.shared.repository.store.SeatIdentityRepository;
import vn.com.buaansach.web.shared.repository.store.SeatRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatIdentityService {
    private final SeatIdentityRepository seatIdentityRepository;
    private final SeatRepository seatRepository;

    private void resetItemAttribute(SeatIdentityEntity entity) {
        entity.setUserAgent(null);
        entity.setPlatform(null);
        entity.setLanguage(null);
        entity.setLanguages(null);
        entity.setDeviceMemory(null);
        entity.setHardwareConcurrency(null);
        entity.setScreenWidth(null);
        entity.setScreenHeight(null);
    }

    public void createSeatIdentity(UUID seatGuid) {
        SeatIdentityEntity entity = new SeatIdentityEntity();
        entity.setSeatGuid(seatGuid);
        seatIdentityRepository.save(entity);
    }

    public void createSeatIdentity(List<UUID> listSeatGuid) {
        List<SeatIdentityEntity> list = listSeatGuid.stream().map(seatGuid -> {
            SeatIdentityEntity entity = new SeatIdentityEntity();
            entity.setSeatGuid(seatGuid);
            return entity;
        }).collect(Collectors.toList());
        seatIdentityRepository.saveAll(list);
    }

    public void createAllSeatIdentity() {
        List<UUID> listSeatIdentityGuid = seatIdentityRepository.findAll()
                .stream()
                .map(SeatIdentityEntity::getSeatGuid)
                .collect(Collectors.toList());
        if (listSeatIdentityGuid.size() == 0) {
            listSeatIdentityGuid.add(UUID.randomUUID());
        }
        List<SeatEntity> listSeat = seatRepository.findByGuidNotInOrderByIdAsc(listSeatIdentityGuid);
        createSeatIdentity(listSeat.stream().map(SeatEntity::getGuid).collect(Collectors.toList()));
    }

    public void updateSeatIdentity(SeatIdentityEntity update) {
        SeatIdentityEntity entity = seatIdentityRepository.findOneBySeatGuid(update.getSeatGuid())
                .orElse(null);
        if (entity == null) return;
        update.setUserAgent(update.getUserAgent().trim());
        update.setPlatform(update.getPlatform().trim());
        update.setLanguage(update.getLanguage().trim());
        update.setId(entity.getId());
        seatIdentityRepository.save(update);
    }

    public void resetSeatIdentity(UUID seatGuid) {
        SeatIdentityEntity entity = seatIdentityRepository.findOneBySeatGuid(seatGuid)
                .orElse(null);
        if (entity == null) return;
        seatIdentityRepository.save(entity);
    }

    public void resetSeatIdentity(List<UUID> listSeatGuid) {
        List<SeatIdentityEntity> list = seatIdentityRepository.findBySeatGuidIn(listSeatGuid);
        list = list.stream().peek(this::resetItemAttribute).collect(Collectors.toList());
        seatIdentityRepository.saveAll(list);
    }

    @Transactional
    public void swapSeatIdentity(SeatEntity source, SeatEntity target) {
        SeatIdentityEntity sourceIdentity = seatIdentityRepository.findOneBySeatGuid(source.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_IDENTITY_NOT_FOUND));

        SeatIdentityEntity targetIdentity = seatIdentityRepository.findOneBySeatGuid(target.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_IDENTITY_NOT_FOUND));

        targetIdentity.setUserAgent(sourceIdentity.getUserAgent());
        targetIdentity.setPlatform(sourceIdentity.getPlatform());
        targetIdentity.setLanguage(sourceIdentity.getLanguage());
        targetIdentity.setLanguages(sourceIdentity.getLanguages());
        targetIdentity.setDeviceMemory(sourceIdentity.getDeviceMemory());
        targetIdentity.setHardwareConcurrency(sourceIdentity.getHardwareConcurrency());
        targetIdentity.setScreenWidth(sourceIdentity.getScreenWidth());
        targetIdentity.setScreenHeight(sourceIdentity.getScreenHeight());

        resetItemAttribute(sourceIdentity);
        seatIdentityRepository.save(targetIdentity);
        seatIdentityRepository.save(sourceIdentity);
    }

    public boolean isSeatIdentityMatched(SeatIdentityEntity identity, UUID seatGuid) {
        SeatIdentityEntity origin = seatIdentityRepository.findOneBySeatGuid(seatGuid)
                .orElse(null);
        if (origin == null) return false;

        return identity.equals(origin);
    }

    public void deleteSeatIdentity(UUID seatGuid) {
        SeatIdentityEntity entity = seatIdentityRepository.findOneBySeatGuid(seatGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_IDENTITY_NOT_FOUND));
        seatIdentityRepository.delete(entity);
    }

    public void deleteSeatIdentity(List<UUID> listSeatGuid) {
        List<SeatIdentityEntity> list = seatIdentityRepository.findBySeatGuidIn(listSeatGuid);
        seatIdentityRepository.deleteAll(list);
    }

}
