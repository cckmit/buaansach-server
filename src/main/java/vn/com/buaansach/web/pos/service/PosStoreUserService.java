package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.store.PosStoreUserRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosStoreUserService {
    private final PosStoreUserRepository posStoreUserRepository;
    private final PosStoreSecurity posStoreSecurity;

    public List<PosStoreUserDTO> getListStoreUserWithUserInfo(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        return posStoreUserRepository.findListDTOByStoreGuid(UUID.fromString(storeGuid));
    }

    public String getCurrentStoreUserRole(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        StoreUserEntity storeUserEntity = posStoreUserRepository.findOneByUserLoginAndStoreGuid(SecurityUtils.getCurrentUserLogin(), UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_USER_NOT_FOUND));
        return storeUserEntity.getStoreUserRole().name();
    }
}
