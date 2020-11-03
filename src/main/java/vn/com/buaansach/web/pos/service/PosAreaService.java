package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.AreaEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.store.PosAreaRepository;
import vn.com.buaansach.web.pos.repository.store.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreUserRepository;
import vn.com.buaansach.web.pos.repository.user.PosUserRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosAreaDTO;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosAreaService {
    private final PosStoreRepository posStoreRepository;
    private final PosAreaRepository posAreaRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosStoreUserRepository posStoreUserRepository;
    private final PosUserRepository posUserRepository;

    public List<PosAreaDTO> getListAreaWithSeatByStoreGuid(UUID storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(storeGuid);

        StoreEntity storeEntity = posStoreRepository.findOneByGuid(storeGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        UserEntity currentUser = posUserRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        StoreUserEntity storeUserEntity = posStoreUserRepository.findOneByUserGuidAndStoreGuid(currentUser.getGuid(), storeGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_USER_NOT_FOUND));

        List<UUID> listAreaGuid;
        if (storeUserEntity.getStoreUserArea() == null || storeUserEntity.getStoreUserArea().isBlank()) listAreaGuid = new ArrayList<>();
        else {
            listAreaGuid = Arrays.stream(storeUserEntity.getStoreUserArea().split(";")).map(UUID::fromString).collect(Collectors.toList());
        }
        List<AreaEntity> listArea = posAreaRepository.findByStoreGuidAndAreaActivatedAndGuidInOrderByAreaPositionAsc(storeEntity.getGuid(), true, listAreaGuid);
        List<PosSeatDTO> listSeat = posSeatRepository.findListPosSeatDTOByStoreGuid(storeEntity.getGuid());
        List<PosAreaDTO> result = new ArrayList<>();
        listArea.forEach(area -> {
            PosAreaDTO dto = new PosAreaDTO(area, listSeat.stream()
                    .filter(seat -> seat.getAreaGuid().equals(area.getGuid()))
                    .collect(Collectors.toList())
            );
            result.add(dto);
        });
        return result;
    }

    public List<PosAreaDTO> getListAreaWithoutSeatByStoreGuid(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));

        StoreEntity storeEntity = posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        return posAreaRepository.findByStoreGuidAndAreaActivated(storeEntity.getGuid(), true)
                .stream()
                .map(PosAreaDTO::new)
                .collect(Collectors.toList());
    }
}
