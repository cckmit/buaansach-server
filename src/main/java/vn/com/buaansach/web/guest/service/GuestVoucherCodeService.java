package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.guest.repository.voucher.GuestVoucherCodeRepository;
import vn.com.buaansach.web.guest.repository.voucher.GuestVoucherRepository;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.guest.websocket.dto.GuestSocketDTO;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GuestVoucherCodeService {
    private final GuestVoucherCodeRepository guestVoucherCodeRepository;
    private final GuestVoucherRepository guestVoucherRepository;
    private final GuestSocketService guestSocketService;

    @Transactional
    public void createVoucherForCustomerRegistration(String customerPhone) {
        VoucherEntity voucherEntity = guestVoucherRepository
                .findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID)
                .orElseThrow();
        /* if voucher is disabled, voucher code wont be created */
//        if (!voucherEntity.isVoucherEnable()) return;
//        voucherEntity.setNumberVoucherCode(guestVoucherCodeRepository.countNumberVoucherCodeByVoucherGuid(voucherEntity.getGuid()) + 1);
//        guestVoucherRepository.save(voucherEntity);
//        VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
//        voucherCodeEntity.setCustomerPhone(customerPhone);
//        voucherCodeEntity.setVoucherCodeUsageCount(0);
//        /* will be set to true later if customer has zalo id */
//        voucherCodeEntity.setVoucherCodeUsable(false);
//        voucherCodeEntity.setVoucherCodeClaimStatus(VoucherCodeClaimStatus.UNSET);
//        voucherCodeEntity.setVoucherGuid(voucherEntity.getGuid());
//        voucherCodeEntity.setVoucherCode(guestVoucherInventoryService.getOneVoucherCode());
//        guestVoucherCodeRepository.save(voucherCodeEntity);

        /* Gửi thông báo tới bộ phận CSKH */
        GuestSocketDTO dto = new GuestSocketDTO();
        dto.setMessage(WebSocketConstants.GUEST_CREATE_CUSTOMER);
        dto.setPayload(customerPhone);
        guestSocketService.sendMessage(WebSocketConstants.TOPIC_CUSTOMER_CARE_TRACKER, dto);
    }
}
