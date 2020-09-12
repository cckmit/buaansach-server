package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreProductStatus;
import vn.com.buaansach.entity.store.StoreProductEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.pos.repository.store.PosStoreProductRepository;
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

    public void changeStoreProductStatus(PosStoreProductStatusChangeDTO payload) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        StoreProductEntity storeProductEntity = posStoreProductRepository.findOneByGuid(payload.getStoreProductGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_PRODUCT_NOT_FOUND));
        storeProductEntity.setStoreProductStatus(payload.getStoreProductStatus());
        posStoreProductRepository.save(storeProductEntity);

        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage(WebSocketConstants.POS_UPDATE_STORE_PRODUCT);
        dto.setPayload(payload);
        posSocketService.sendMessage(WebSocketConstants.TOPIC_GUEST_PREFIX + storeProductEntity.getStoreGuid(), dto);
    }
}
