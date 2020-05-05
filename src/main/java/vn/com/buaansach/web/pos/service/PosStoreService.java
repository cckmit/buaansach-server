package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.service.StoreSecurityService;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreStatusChangeDTO;

import java.util.UUID;

@Service
public class PosStoreService {
    private final PosStoreRepository posStoreRepository;
    private final StoreSecurityService storeSecurityService;

    public PosStoreService(PosStoreRepository posStoreRepository, StoreSecurityService storeSecurityService) {
        this.posStoreRepository = posStoreRepository;
        this.storeSecurityService = storeSecurityService;
    }

    public PosStoreDTO getStore(String storeGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        return new PosStoreDTO(storeEntity);
    }

    public void changeStoreStatus(PosStoreStatusChangeDTO payload) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + payload.getStoreGuid()));
        storeSecurityService.blockAccessIfNotInStore(payload.getStoreGuid());
        storeEntity.setStoreStatus(payload.getStoreStatus());
        posStoreRepository.save(storeEntity);
    }
}
