package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreStatusChangeDTO;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.pos.websocket.dto.PosSocketDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosStoreService {
    private final PosStoreRepository posStoreRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosSocketService posSocketService;

    public PosStoreDTO getStore(String storeGuid) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException("pos@storeNotFound@" + storeGuid));
        return new PosStoreDTO(storeEntity);
    }

    public void changeStoreStatus(PosStoreStatusChangeDTO payload) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException("pos@storeNotFound@" + payload.getStoreGuid()));
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        storeEntity.setStoreStatus(payload.getStoreStatus());
        posStoreRepository.save(storeEntity);

        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage(WebSocketConstants.POS_UPDATE_STORE_STATUS);
        dto.setPayload(payload);
        posSocketService.sendMessage(WebSocketConstants.TOPIC_GUEST_PREFIX + storeEntity.getGuid(), dto);
    }
}
