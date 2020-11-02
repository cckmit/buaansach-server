package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreWorkShiftEntity;
import vn.com.buaansach.entity.store.StoreWorkShiftUserEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.store.AdminStoreWorkShiftRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreWorkShiftUserRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateStoreWorkShiftUserDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStoreWorkShiftUserService {
    private final AdminStoreWorkShiftUserRepository adminStoreWorkShiftUserRepository;
    private final AdminStoreWorkShiftRepository adminStoreWorkShiftRepository;

    @Transactional
    public void updateStoreWorkShiftUser(AdminUpdateStoreWorkShiftUserDTO payload){
        StoreWorkShiftEntity storeWorkShift =adminStoreWorkShiftRepository.findOneByGuid(payload.getStoreWorkShiftGuid())
                .orElseThrow(()-> new NotFoundException(ErrorCode.STORE_WORK_SHIFT_NOT_FOUND));
        adminStoreWorkShiftUserRepository.deleteByStoreWorkShiftGuid(payload.getStoreWorkShiftGuid());
        List<StoreWorkShiftUserEntity> list = new ArrayList<>();

        int index = 0;
        for (String userLogin: payload.getListUser()){
            StoreWorkShiftUserEntity entity = new StoreWorkShiftUserEntity();
            entity.setWorkDay(payload.getListWorkDay().get(index));
            entity.setStoreGuid(storeWorkShift.getStoreGuid());
            entity.setUserLogin(userLogin);
            entity.setStoreWorkShiftGuid(payload.getStoreWorkShiftGuid());
            list.add(entity);
            index++;
        }

        adminStoreWorkShiftUserRepository.saveAll(list);
    }
}
