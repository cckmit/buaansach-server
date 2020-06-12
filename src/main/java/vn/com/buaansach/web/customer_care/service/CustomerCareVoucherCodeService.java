package vn.com.buaansach.web.customer_care.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.VoucherCodeClaimStatus;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.customer_care.repository.CustomerCareVoucherCodeRepository;
import vn.com.buaansach.web.customer_care.service.dto.CustomerCareUpdateVoucherCodeDTO;

@Service
@RequiredArgsConstructor
public class CustomerCareVoucherCodeService {
    private final CustomerCareVoucherCodeRepository customerCareVoucherCodeRepository;

    public void updateFirstRegVoucherCode(CustomerCareUpdateVoucherCodeDTO payload) {
        VoucherCodeEntity voucherCodeEntity = customerCareVoucherCodeRepository.findOneByVoucherCode(payload.getVoucherCode())
                .orElseThrow(() -> new ResourceNotFoundException("customerCare@voucherCodeNotFound@" + payload.getVoucherCode()));
        voucherCodeEntity.setVoucherCodeClaimStatus(payload.getVoucherCodeClaimStatus());
        if (payload.getVoucherCodeClaimStatus().equals(VoucherCodeClaimStatus.CLAIMED)) {
            voucherCodeEntity.setVoucherCodeUsable(true);
        } else {
            voucherCodeEntity.setVoucherCodeUsable(false);
        }
        customerCareVoucherCodeRepository.save(voucherCodeEntity);
    }
}
