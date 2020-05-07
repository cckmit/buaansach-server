package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.web.admin.service.StoreSecurityService;
import vn.com.buaansach.web.pos.repository.PosVoucherCodeRepository;
import vn.com.buaansach.web.pos.repository.PosVoucherRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class PosVoucherCodeService {
    private final StoreSecurityService storeSecurityService;
    private final PosVoucherCodeRepository posVoucherCodeRepository;
    private final PosVoucherRepository posVoucherRepository;
    private final PosVoucherInventoryService posVoucherInventoryService;

    public PosVoucherCodeService(StoreSecurityService storeSecurityService, PosVoucherCodeRepository posVoucherCodeRepository, PosVoucherRepository posVoucherRepository, PosVoucherInventoryService posVoucherInventoryService) {
        this.storeSecurityService = storeSecurityService;
        this.posVoucherCodeRepository = posVoucherCodeRepository;
        this.posVoucherRepository = posVoucherRepository;
        this.posVoucherInventoryService = posVoucherInventoryService;
    }

    public PosVoucherCodeDTO getVoucherCodeInfo(String voucherCode) {
        return posVoucherCodeRepository.getPosVoucherCodeDTO(voucherCode);
    }

    @Transactional
    public void createVoucherForCustomerRegistration(String customerPhone) {
        VoucherEntity voucherEntity = posVoucherRepository.selectForUpdate(1L).orElseThrow();
        posVoucherRepository.increaseNumberVoucherCode(1L);
        VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
        voucherCodeEntity.setCustomerPhone(customerPhone);
        voucherCodeEntity.setVoucherCodeUsageCount(0);
        voucherCodeEntity.setVoucherCodeUsable(true);
        voucherCodeEntity.setVoucherGuid(voucherEntity.getGuid());
        voucherCodeEntity.setVoucherCode(posVoucherInventoryService.getOneVoucherCode());
        posVoucherCodeRepository.save(voucherCodeEntity);
    }
}
