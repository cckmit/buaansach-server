package vn.com.buaansach.web.manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.manager.repository.ManagerVoucherCodeRepository;
import vn.com.buaansach.web.manager.service.dto.ManagerUpdateVoucherCodeDTO;

@Service
@RequiredArgsConstructor
public class ManagerVoucherCodeService {
    private final ManagerVoucherCodeRepository managerVoucherCodeRepository;

    public void updateFirstRegVoucherCode(ManagerUpdateVoucherCodeDTO payload) {
        VoucherCodeEntity voucherCodeEntity = managerVoucherCodeRepository.findOneByVoucherCode(payload.getVoucherCode())
                .orElseThrow(() -> new ResourceNotFoundException("manager@voucherCodeNotFound@" + payload.getVoucherCode()));
        voucherCodeEntity.setVoucherCodeSentStatus(payload.getVoucherCodeSentStatus());
        if (payload.getVoucherCodeSentStatus().equals(VoucherCodeSentStatus.SENT)) {
            voucherCodeEntity.setVoucherCodeUsable(true);
        } else {
            voucherCodeEntity.setVoucherCodeUsable(false);
        }
        managerVoucherCodeRepository.save(voucherCodeEntity);
    }
}
