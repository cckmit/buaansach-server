package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.guest.repository.voucher.GuestVoucherRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestVoucherDTO;

@Service
@RequiredArgsConstructor
public class GuestVoucherService {
    private final GuestVoucherRepository guestVoucherRepository;

    public GuestVoucherDTO getFirstRegisterVoucher() {
        VoucherEntity voucherEntity = guestVoucherRepository.findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID)
                .orElseThrow(() -> new NotFoundException("guest@voucherNotFound@" + Constants.DEFAULT_FIRST_REG_VOUCHER_ID));
        return new GuestVoucherDTO(voucherEntity);
    }
}
