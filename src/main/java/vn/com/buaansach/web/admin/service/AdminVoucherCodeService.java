package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.repository.voucher.AdminVoucherCodeRepository;

@Service
@RequiredArgsConstructor
public class AdminVoucherCodeService {
    private final AdminVoucherCodeRepository adminVoucherCodeRepository;

    public void toggleVoucherCode(String voucherCode) {
        VoucherCodeEntity voucherCodeEntity = adminVoucherCodeRepository.findOneByVoucherCode(voucherCode)
                .orElseThrow(() -> new ResourceNotFoundException("admin@voucherCodeNotFound@" + voucherCode));
        voucherCodeEntity.setVoucherCodeUsable(!voucherCodeEntity.isVoucherCodeUsable());
        adminVoucherCodeRepository.save(voucherCodeEntity);
    }
}
