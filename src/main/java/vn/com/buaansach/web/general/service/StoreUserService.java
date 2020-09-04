package vn.com.buaansach.web.general.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;
import vn.com.buaansach.web.general.repository.store.GeneralStoreUserRepository;
import vn.com.buaansach.web.general.service.dto.read.StoreUserDTO;

import java.util.List;

@Service
public class StoreUserService {
    private final GeneralStoreUserRepository generalStoreUserRepository;

    public StoreUserService(GeneralStoreUserRepository generalStoreUserRepository) {
        this.generalStoreUserRepository = generalStoreUserRepository;
    }

    public List<StoreUserDTO> getListStoreUserByUser(String currentUser) {
        return generalStoreUserRepository.findListStoreUserByUser(currentUser, true, StoreUserStatus.WORKING);
    }
}
