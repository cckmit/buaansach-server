package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.store.StoreWorkShiftEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.store.AdminStoreWorkShiftRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreWorkShiftUserRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreWorkShiftDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStoreWorkShiftService {
    private final AdminStoreWorkShiftRepository adminStoreWorkShiftRepository;
    private final AdminStoreWorkShiftUserRepository adminStoreWorkShiftUserRepository;

    public StoreWorkShiftEntity createStoreWorkShift(StoreWorkShiftEntity payload) {
        payload.setGuid(UUID.randomUUID());
        return adminStoreWorkShiftRepository.save(payload);
    }

    public List<AdminStoreWorkShiftDTO> getListStoreWorkShiftByStore(UUID storeGuid) {
        List<StoreWorkShiftEntity> listEntity = adminStoreWorkShiftRepository.findByStoreGuid(storeGuid);
        List<AdminStoreWorkShiftDTO> result;
        result = listEntity.stream().map(AdminStoreWorkShiftDTO::new).collect(Collectors.toList());
        result.forEach(item -> {
            item.setListUser(adminStoreWorkShiftUserRepository.findListAdminStoreWorkShiftUserDTO(item.getGuid()));
        });
        return result;
    }

    public StoreWorkShiftEntity updateStoreWorkShift(StoreWorkShiftEntity payload) {
        StoreWorkShiftEntity shiftEntity = adminStoreWorkShiftRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_WORK_SHIFT_NOT_FOUND));
        payload.setId(shiftEntity.getId());
        return adminStoreWorkShiftRepository.save(payload);
    }

    @Transactional
    public void deleteStoreWorkShift(UUID storeWorkShiftGuid) {
        adminStoreWorkShiftUserRepository.deleteByStoreWorkShiftGuid(storeWorkShiftGuid);
        adminStoreWorkShiftRepository.deleteByGuid(storeWorkShiftGuid);
    }
}
