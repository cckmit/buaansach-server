package vn.com.buaansach.web.guest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.store.StoreEntity;
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

    public void blockAccessIfStoreIsNotOpenOrDeactivated(UUID storeGuid) {
        if (isClosedOrDeactivated(storeGuid)) throw new ForbiddenException(ErrorCode.STORE_CLOSED_OR_DEACTIVATED);
    }

}
