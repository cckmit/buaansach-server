package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.store.AdminAreaRepository;
import vn.com.buaansach.web.admin.repository.store.AdminSeatRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateSeatDTO;
import vn.com.buaansach.web.shared.service.SeatIdentityService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSeatService {
    private final AdminSeatRepository adminSeatRepository;
    private final AdminAreaRepository adminAreaRepository;
    private final SeatIdentityService seatIdentityService;

    @Transactional
    public SeatEntity createSeat(AdminCreateSeatDTO request) {
        adminAreaRepository.findOneByGuid(request.getAreaGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

        SeatEntity seatEntity = new SeatEntity();
        UUID guid = UUID.randomUUID();
        seatEntity.setGuid(guid);
        seatEntity.setSeatName(request.getSeatName());
        seatEntity.setSeatNameEng(request.getSeatNameEng());
        seatEntity.setSeatStatus(SeatStatus.EMPTY);
        seatEntity.setSeatServiceStatus(SeatServiceStatus.FINISHED);
        seatEntity.setSeatLocked(false);
        seatEntity.setOrderGuid(null);
        seatEntity.setAreaGuid(request.getAreaGuid());

        seatIdentityService.createSeatIdentity(guid);
        return adminSeatRepository.save(seatEntity);
    }

    public SeatEntity updateSeat(SeatEntity updateEntity) {
        SeatEntity currentEntity = adminSeatRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
        currentEntity.setSeatName(updateEntity.getSeatName());
        currentEntity.setSeatNameEng(updateEntity.getSeatNameEng());
        return adminSeatRepository.save(currentEntity);
    }

    @Transactional
    public void deleteSeat(UUID seatGuid) {
        seatIdentityService.deleteSeatIdentity(seatGuid);
        adminSeatRepository.deleteByGuid(seatGuid);
    }

    @Transactional
    public void deleteByStore(UUID storeGuid) {
        List<SeatEntity> list = adminSeatRepository.findListSeatByStoreGuid(storeGuid);
        seatIdentityService.deleteSeatIdentity(list.stream().map(SeatEntity::getGuid).collect(Collectors.toList()));
        adminSeatRepository.deleteAll(list);
    }

    @Transactional
    public void deleteByArea(UUID areaGuid) {
        List<SeatEntity> list = adminSeatRepository.findByAreaGuid(areaGuid);
        seatIdentityService.deleteSeatIdentity(list.stream().map(SeatEntity::getGuid).collect(Collectors.toList()));
        adminSeatRepository.deleteAll(list);
    }
}
