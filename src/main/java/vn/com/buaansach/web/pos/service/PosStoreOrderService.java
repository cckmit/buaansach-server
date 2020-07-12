package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;
import vn.com.buaansach.entity.enumeration.StoreOrderType;
import vn.com.buaansach.entity.store.StoreOrderEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosStoreOrderRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreOrderDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreOrderStatusUpdateDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreOrderVisibilityUpdateDTO;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosStoreOrderService {
    private final PosStoreOrderRepository posStoreOrderRepository;
    private final PosStoreSecurity posStoreSecurity;

    public List<PosStoreOrderDTO> getListStoreOrder(String storeGuid, Instant startDate, Boolean hidden) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        List<StoreOrderEntity> list;
        if (hidden != null) {
            list = posStoreOrderRepository
                    .findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate, hidden);
        } else {
            list = posStoreOrderRepository
                    .findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate);
        }
        return list.stream().map(PosStoreOrderDTO::new).collect(Collectors.toList());
    }

    public StoreOrderEntity createStoreOrder(UUID storeGuid, UUID areaGuid, UUID seatGuid, UUID orderGuid, UUID orderProductGroup, int numberOfProduct) {
        StoreOrderEntity storeOrderEntity = new StoreOrderEntity();
        storeOrderEntity.setGuid(UUID.randomUUID());
        storeOrderEntity.setStoreOrderStatus(StoreOrderStatus.UNSEEN);
        storeOrderEntity.setStoreOrderType(StoreOrderType.POS);
        storeOrderEntity.setHidden(false);
        storeOrderEntity.setStoreGuid(storeGuid);
        storeOrderEntity.setAreaGuid(areaGuid);
        storeOrderEntity.setSeatGuid(seatGuid);
        storeOrderEntity.setOrderGuid(orderGuid);
        storeOrderEntity.setOrderProductGroup(orderProductGroup);
        storeOrderEntity.setNumberOfProduct(numberOfProduct);
        return posStoreOrderRepository.save(storeOrderEntity);
    }

    public PosStoreOrderDTO updateStoreOrder(PosStoreOrderStatusUpdateDTO payload, String currentUser) {
        StoreOrderEntity entity = posStoreOrderRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeOrderNotFound@" + payload.getGuid()));
        posStoreSecurity.blockAccessIfNotInStore(entity.getStoreGuid());
        entity.setStoreOrderStatus(payload.getStoreOrderStatus());
        if (entity.getFirstSeenBy() == null && payload.getStoreOrderStatus().equals(StoreOrderStatus.SEEN))
            entity.setFirstSeenBy(currentUser);
        return new PosStoreOrderDTO(posStoreOrderRepository.save(entity));
    }

    @Transactional
    public void toggleVisibility(PosStoreOrderVisibilityUpdateDTO payload, String currentUser) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<StoreOrderEntity> list = posStoreOrderRepository.findByGuidIn(payload.getListGuid());

        list.forEach(item -> {
            if (!item.getStoreGuid().equals(payload.getStoreGuid()))
                throw new BadRequestException("pos@storeOrderNotInStore@" + payload.getStoreGuid());
        });

        list = list.stream().peek(item -> {
            item.setHidden(payload.isHidden());
            if (item.getFirstHideBy() == null && payload.isHidden()) item.setFirstHideBy(currentUser);
        }).collect(Collectors.toList());

        posStoreOrderRepository.saveAll(list);
    }
}
