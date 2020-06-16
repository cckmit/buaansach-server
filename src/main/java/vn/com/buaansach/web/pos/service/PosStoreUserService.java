package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreUserEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.PosStoreUserRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreUserDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosStoreUserService {
    private final PosStoreUserRepository posStoreUserRepository;

    public List<PosStoreUserDTO> getListStoreUser(String storeGuid) {
        List<StoreUserEntity> listStoreUser = posStoreUserRepository.findByStoreGuid(UUID.fromString(storeGuid));
        return listStoreUser.stream().map(PosStoreUserDTO::new).collect(Collectors.toList());
    }

    public List<PosStoreUserDTO> getListStoreUserWithUserInfo(String storeGuid) {
        return posStoreUserRepository.findListDTOByStoreGuid(UUID.fromString(storeGuid));
    }

    public String getCurrentStoreUserRole(String storeGuid) {
        StoreUserEntity storeUserEntity = posStoreUserRepository.findOneByUserLoginAndStoreGuid(SecurityUtils.getCurrentUserLogin(), UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeUserNotFound@" + storeGuid + ";" + SecurityUtils.getCurrentUserLogin()));
        return storeUserEntity.getStoreUserRole().name();
    }
}
