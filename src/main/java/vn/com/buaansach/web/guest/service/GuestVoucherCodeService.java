package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.guest.repository.GuestVoucherCodeRepository;
import vn.com.buaansach.web.guest.repository.GuestVoucherRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GuestVoucherCodeService {
    private final GuestVoucherCodeRepository guestVoucherCodeRepository;
    private final GuestVoucherRepository guestVoucherRepository;
    private final GuestVoucherInventoryService guestVoucherInventoryService;

    @Transactional
    public void createVoucherForCustomerRegistration(String customerPhone) {
        VoucherEntity voucherEntity = guestVoucherRepository
                .findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID)
                .orElseThrow();
        /* if voucher is disabled, voucher code wont be created */
        if (!voucherEntity.isVoucherEnable()) return;
        voucherEntity.setNumberVoucherCode(guestVoucherCodeRepository.countNumberVoucherCodeByVoucherGuid(voucherEntity.getGuid()));
        guestVoucherRepository.save(voucherEntity);
        VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
        voucherCodeEntity.setCustomerPhone(customerPhone);
        voucherCodeEntity.setVoucherCodeUsageCount(0);
        /* will be set to true later if customer has zalo id */
        voucherCodeEntity.setVoucherCodeUsable(false);
        voucherCodeEntity.setVoucherGuid(voucherEntity.getGuid());
        voucherCodeEntity.setVoucherCode(guestVoucherInventoryService.getOneVoucherCode());
        guestVoucherCodeRepository.save(voucherCodeEntity);
    }
}
