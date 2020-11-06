package vn.com.buaansach.web.guest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SeatStatus;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.ForbiddenException;
import vn.com.buaansach.web.guest.repository.store.GuestStoreRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreSecurity {
    private final GuestStoreRepository guestStoreRepository;

    private boolean isClosedOrDeactivated(UUID storeGuid) {
        StoreEntity storeEntity = guestStoreRepository.findOneByGuid(storeGuid).orElse(null);
        return storeEntity == null || storeEntity.getStoreStatus().equals(StoreStatus.CLOSED) || !storeEntity.isStoreActivated();
    }

    private boolean isClosedOrDeactivated(StoreEntity storeEntity) {
        return storeEntity == null || storeEntity.getStoreStatus().equals(StoreStatus.CLOSED) || !storeEntity.isStoreActivated();
    }

    public void blockAccessIfStoreAbnormal(UUID storeGuid) {
        if (isClosedOrDeactivated(storeGuid)) throw new ForbiddenException(ErrorCode.STORE_CLOSED_OR_DEACTIVATED);
    }

    public void blockAccessIfStoreAbnormal(StoreEntity storeEntity) {
        if (isClosedOrDeactivated(storeEntity)) throw new ForbiddenException(ErrorCode.STORE_CLOSED_OR_DEACTIVATED);
    }

    public void blockAccessIfSeatAbnormal(SeatEntity seatEntity){
        if (seatEntity.isSeatLocked())
            throw new BadRequestException(ErrorCode.SEAT_LOCKED);
        if (seatEntity.getSeatStatus().equals(SeatStatus.NON_EMPTY))
            throw new BadRequestException(ErrorCode.SEAT_NON_EMPTY);
    }

    public void blockAccessIfAreaAbnormal(AreaEntity areaEntity){
        if (!areaEntity.isAreaActivated())
            throw new BadRequestException(ErrorCode.AREA_DISABLED);
    }

}
