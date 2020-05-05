package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.web.admin.service.StoreSecurityService;
import vn.com.buaansach.web.pos.repository.PosVoucherCodeRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;

import java.util.UUID;

@Service
public class PosVoucherService {
    private final StoreSecurityService storeSecurityService;
    private final PosVoucherCodeRepository posVoucherCodeRepository;

    public PosVoucherService(StoreSecurityService storeSecurityService, PosVoucherCodeRepository posVoucherCodeRepository) {
        this.storeSecurityService = storeSecurityService;
        this.posVoucherCodeRepository = posVoucherCodeRepository;
    }

    public PosVoucherCodeDTO getVoucherCodeInfo(String storeGuid, String voucherCode) {
        storeSecurityService.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        return posVoucherCodeRepository.getPosVoucherCodeDTO(voucherCode);
    }
}
