package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreStatusChangeDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosStoreService {
    private final PosStoreRepository posStoreRepository;
    private final PosStoreSecurity posStoreSecurity;

    public PosStoreDTO getStore(String storeGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFound@" + storeGuid));
        return new PosStoreDTO(storeEntity);
    }

    public void changeStoreStatus(PosStoreStatusChangeDTO payload) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFound@" + payload.getStoreGuid()));
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        storeEntity.setStoreStatus(payload.getStoreStatus());
        posStoreRepository.save(storeEntity);
    }
}
