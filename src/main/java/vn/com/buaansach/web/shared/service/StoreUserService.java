package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.shared.repository.store.StoreUserRepository;
import vn.com.buaansach.web.shared.service.dto.read.StoreUserDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreUserService {
    private final StoreUserRepository storeUserRepository;

    public List<StoreUserDTO> getListStoreUserByUser(String currentUser) {
        return storeUserRepository.findListStoreUserByUser(currentUser, true, StoreUserStatus.WORKING);
    }
}
