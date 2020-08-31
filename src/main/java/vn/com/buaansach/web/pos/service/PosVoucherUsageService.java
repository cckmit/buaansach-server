package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherUsageEntity;
import vn.com.buaansach.web.pos.repository.PosVoucherUsageRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosVoucherUsageService {
    private final PosVoucherUsageRepository posVoucherUsageRepository;

    public void addVoucherUsage(String voucherCode, UUID orderGuid, String usedBy) {
        /* voucher code usage */
        VoucherUsageEntity voucherUsageEntity = new VoucherUsageEntity();
        voucherUsageEntity.setVoucherCode(voucherCode);
        voucherUsageEntity.setOrderGuid(orderGuid);
//        voucherUsageEntity.setUsedDate(Instant.now());
        voucherUsageEntity.setUsedBy(usedBy);
        posVoucherUsageRepository.save(voucherUsageEntity);
    }
}
