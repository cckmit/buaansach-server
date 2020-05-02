package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.web.admin.service.StoreUserSecurityService;
import vn.com.buaansach.web.pos.repository.PosVoucherCodeRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;

import java.util.UUID;

@Service
public class PosVoucherService {
    private final StoreUserSecurityService storeUserSecurityService;
    private final PosVoucherCodeRepository posVoucherCodeRepository;

    public PosVoucherService(StoreUserSecurityService storeUserSecurityService, PosVoucherCodeRepository posVoucherCodeRepository) {
        this.storeUserSecurityService = storeUserSecurityService;
        this.posVoucherCodeRepository = posVoucherCodeRepository;
    }

    public PosVoucherCodeDTO getVoucherCodeInfo(String storeGuid, String voucherCode) {
        storeUserSecurityService.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        return posVoucherCodeRepository.getPosVoucherCodeDTO(voucherCode);
    }
}
