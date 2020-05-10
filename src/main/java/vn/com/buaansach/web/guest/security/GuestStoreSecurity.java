package vn.com.buaansach.web.guest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreStatus;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.AccessDeniedException;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreSecurity {
    private final PosStoreRepository posStoreRepository;

    private boolean isClosedOrDeactivated(UUID storeGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(storeGuid).orElse(null);
        return storeEntity == null || storeEntity.getStoreStatus().equals(StoreStatus.CLOSED) || !storeEntity.isStoreActivated();
    }

    public void blockAccessIfStoreIsNotOpenOrDeactivated(UUID storeGuid) {
        if (isClosedOrDeactivated(storeGuid)) throw new AccessDeniedException("Store has been closed");
    }

}
