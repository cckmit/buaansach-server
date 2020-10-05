package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.WebSocketEndpoints;
import vn.com.buaansach.util.WebSocketMessages;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreStatusChangeDTO;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.shared.websocket.dto.DataSocketDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosStoreService {
    private final PosStoreRepository posStoreRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosSocketService posSocketService;

    public PosStoreDTO getStore(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        return new PosStoreDTO(storeEntity);
    }

    public void changeStoreStatus(PosStoreStatusChangeDTO payload) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        storeEntity.setStoreStatus(payload.getStoreStatus());
        posStoreRepository.save(storeEntity);

        DataSocketDTO dto = new DataSocketDTO();
        dto.setMessage(WebSocketMessages.POS_UPDATE_STORE_STATUS);
        dto.setPayload(payload);
        posSocketService.sendMessage(WebSocketEndpoints.TOPIC_GUEST_PREFIX + storeEntity.getGuid(), dto);
    }
}
