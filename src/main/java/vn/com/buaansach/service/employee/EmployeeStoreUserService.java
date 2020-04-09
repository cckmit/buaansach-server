package vn.com.buaansach.service.employee;

import org.springframework.stereotype.Service;
import vn.com.buaansach.repository.StoreUserRepository;
import vn.com.buaansach.service.dto.EmployeeStoreUserDTO;

import java.util.List;

@Service
public class EmployeeStoreUserService {
    private final StoreUserRepository storeUserRepository;

    public EmployeeStoreUserService(StoreUserRepository storeUserRepository) {
        this.storeUserRepository = storeUserRepository;
    }

    public List<EmployeeStoreUserDTO> getListStoreUserByUser(String currentUser) {
        return storeUserRepository.findListStoreUserByUser(currentUser);
    }
}
