package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosVoucherRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherDTO;

@Service
@RequiredArgsConstructor
public class PosVoucherService {
    private final PosVoucherRepository posVoucherRepository;

    public PosVoucherDTO getFirstRegisterVoucher() {
        VoucherEntity voucherEntity = posVoucherRepository.findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID)
                .orElseThrow(() -> new GuestResourceNotFoundException("pos@voucherNotFound@" + Constants.DEFAULT_FIRST_REG_VOUCHER_ID));
        return new PosVoucherDTO(voucherEntity);
    }
}
