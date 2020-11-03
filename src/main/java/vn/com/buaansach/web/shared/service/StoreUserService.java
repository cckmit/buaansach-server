package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.shared.repository.store.StoreUserRepository;
import vn.com.buaansach.web.shared.repository.user.UserRepository;
import vn.com.buaansach.web.shared.service.dto.read.StoreUserDTO;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreUserService {
    private final StoreUserRepository storeUserRepository;
    private final UserRepository userRepository;

    public List<StoreUserDTO> getListStoreUserByUser(String currentUser) {
        UserEntity userEntity = userRepository.findOneByUserLoginIgnoreCase(currentUser)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return storeUserRepository.findListStoreUserDTOByUser(userEntity.getGuid(), true, StoreUserStatus.WORKING);
    }
}
