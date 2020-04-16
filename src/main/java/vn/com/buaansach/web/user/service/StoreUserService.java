package vn.com.buaansach.web.user.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.web.user.repository.StoreUserRepository;
import vn.com.buaansach.web.user.service.dto.StoreUserDTO;

import java.util.List;

@Service
public class StoreUserService {
    private final StoreUserRepository storeUserRepository;

    public StoreUserService(StoreUserRepository storeUserRepository) {
        this.storeUserRepository = storeUserRepository;
    }

    public List<StoreUserDTO> getListStoreUserByUser(String currentUser) {
        return storeUserRepository.findListStoreUserByUser(currentUser);
    }
}
