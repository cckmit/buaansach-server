package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreOrderStatus;
import vn.com.buaansach.entity.enumeration.StorePayRequestStatus;
import vn.com.buaansach.entity.store.StoreOrderEntity;
import vn.com.buaansach.entity.store.StorePayRequestEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosStorePayRequestRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreOrderDTO;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStorePayRequestDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStorePayRequestStatusUpdateDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStorePayRequestVisibilityUpdateDTO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosStorePayRequestService {
    private final Logger log = LoggerFactory.getLogger(PosStorePayRequestService.class);
    private final PosStoreSecurity posStoreSecurity;
    private final PosStorePayRequestRepository posStorePayRequestRepository;

    public List<PosStorePayRequestDTO> getListStorePayRequest(String storeGuid, Instant startDate, Boolean hidden) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        List<StorePayRequestEntity> list;
        if (hidden != null) {
            list = posStorePayRequestRepository
                    .findByStoreGuidAndCreatedDateGreaterThanEqualAndHiddenOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate, hidden);
        } else {
            list = posStorePayRequestRepository
                    .findByStoreGuidAndCreatedDateGreaterThanEqualOrderByCreatedDateAsc(UUID.fromString(storeGuid), startDate);
        }
        return list.stream().map(PosStorePayRequestDTO::new).collect(Collectors.toList());
    }

    public PosStorePayRequestDTO updateStorePayRequest(PosStorePayRequestStatusUpdateDTO payload, String currentUser) {
        StorePayRequestEntity entity = posStorePayRequestRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storePayRequestNotFound@" + payload.getGuid()));
        posStoreSecurity.blockAccessIfNotInStore(entity.getStoreGuid());
        entity.setStorePayRequestStatus(payload.getStorePayRequestStatus());
        if (entity.getFirstSeenBy() == null && payload.getStorePayRequestStatus().equals(StorePayRequestStatus.SEEN)){
            entity.setFirstSeenBy(currentUser);
            entity.setSeenAt(Instant.now());
        }
        return new PosStorePayRequestDTO(posStorePayRequestRepository.save(entity));
    }

    public void toggleVisibility(PosStorePayRequestVisibilityUpdateDTO payload, String currentUser) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<StorePayRequestEntity> list = posStorePayRequestRepository.findByGuidIn(payload.getListGuid());

        list.forEach(item -> {
            if (!item.getStoreGuid().equals(payload.getStoreGuid()))
                throw new BadRequestException("pos@storePayRequestNotInStore@" + payload.getStoreGuid());
        });

        list = list.stream().peek(item -> {
            item.setHidden(payload.isHidden());
            if (item.getFirstHideBy() == null && payload.isHidden()) item.setFirstHideBy(currentUser);
        }).collect(Collectors.toList());

        posStorePayRequestRepository.saveAll(list);
    }

    /**
     * Old store orders should be automatically deleted after 7 days.
     * <p>
     * This is scheduled to get fired at 2:00 (am).
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void removeOldStorePayRequest() {
        Instant deletePoint = Instant.now().minus(7, ChronoUnit.DAYS);
        List<StorePayRequestEntity> list = posStorePayRequestRepository
                .findByCreatedDateBefore(deletePoint);
        log.debug("[Scheduling] Deleting old store pay request data [{}]", list);
        posStorePayRequestRepository.deleteAll(list);
    }
}
