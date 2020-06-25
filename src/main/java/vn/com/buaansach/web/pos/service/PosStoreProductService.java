package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.pos.repository.PosStoreProductRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreProductStatusChangeDTO;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.pos.websocket.dto.PosSocketDTO;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosStoreProductService {
    private final PosStoreProductRepository posStoreProductRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosSocketService posSocketService;

    public List<PosStoreProductDTO> getListStoreProductByStoreGuid(String storeGuid) {
        return posStoreProductRepository.findListPosStoreProductDTO(UUID.fromString(storeGuid), StoreProductStatus.STOP_TRADING);
    }

    public void changeStoreProductStatus(PosStoreProductStatusChangeDTO payload) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        StoreProductEntity storeProductEntity = posStoreProductRepository.findOneByGuid(payload.getStoreProductGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeProductNotFound@" + payload.getStoreProductGuid()));
        storeProductEntity.setStoreProductStatus(payload.getStoreProductStatus());
        posStoreProductRepository.save(storeProductEntity);

        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage(WebSocketConstants.POS_UPDATE_STORE_PRODUCT);
        dto.setPayload(payload);
        posSocketService.sendMessage(WebSocketConstants.TOPIC_GUEST_PREFIX + storeProductEntity.getStoreGuid(), dto);
    }
}
