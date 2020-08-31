package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.voucher.AdminVoucherCodeRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateVoucherCodeDTO;

@Service
@RequiredArgsConstructor
public class AdminVoucherCodeService {
    private final AdminVoucherCodeRepository adminVoucherCodeRepository;

    public void toggleVoucherCode(String voucherCode) {
        VoucherCodeEntity voucherCodeEntity = adminVoucherCodeRepository.findOneByVoucherCode(voucherCode)
                .orElseThrow(() -> new NotFoundException("admin@voucherCodeNotFound@" + voucherCode));
//        voucherCodeEntity.setVoucherCodeUsable(!voucherCodeEntity.isVoucherCodeUsable());
        adminVoucherCodeRepository.save(voucherCodeEntity);
    }

    public void updateVoucherCode(AdminUpdateVoucherCodeDTO payload){
        VoucherCodeEntity voucherCodeEntity = adminVoucherCodeRepository.findOneByVoucherCode(payload.getVoucherCode())
                .orElseThrow(() -> new NotFoundException("admin@voucherCodeNotFound@" + payload.getVoucherCode()));
//        voucherCodeEntity.setVoucherCodeClaimStatus(payload.getVoucherCodeClaimStatus());
        adminVoucherCodeRepository.save(voucherCodeEntity);
    }
}
