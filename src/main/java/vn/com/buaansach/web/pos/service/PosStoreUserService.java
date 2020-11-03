package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.store.PosStoreUserRepository;
import vn.com.buaansach.web.pos.repository.user.PosUserRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosStoreUserService {
    private final PosStoreUserRepository posStoreUserRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosUserRepository posUserRepository;

    public List<PosStoreUserDTO> getListStoreUserWithUserInfo(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        return posStoreUserRepository.findListDTOByStoreGuid(UUID.fromString(storeGuid));
    }

    public String getCurrentStoreUserRole(String storeGuid) {
        posStoreSecurity.blockAccessIfNotInStore(UUID.fromString(storeGuid));

        UserEntity currentUser = posUserRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        StoreUserEntity storeUserEntity = posStoreUserRepository.findOneByUserGuidAndStoreGuid(currentUser.getGuid(), UUID.fromString(storeGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_USER_NOT_FOUND));
        return storeUserEntity.getStoreUserRole().name();
    }
}
