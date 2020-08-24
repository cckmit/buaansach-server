package vn.com.buaansach.web.common.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.common.repository.CommonStoreUserRepository;
import vn.com.buaansach.web.common.service.dto.read.StoreUserDTO;

import java.util.List;

@Service
public class StoreUserService {
    private final CommonStoreUserRepository commonStoreUserRepository;

    public StoreUserService(CommonStoreUserRepository commonStoreUserRepository) {
        this.commonStoreUserRepository = commonStoreUserRepository;
    }

    public List<StoreUserDTO> getListStoreUserByUser(String currentUser) {
        return commonStoreUserRepository.findListStoreUserByUser(currentUser, true, StoreUserStatus.WORKING);
    }
}
