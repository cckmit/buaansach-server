package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.order.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.repository.voucher.PosVoucherCodeRepository;
import vn.com.buaansach.web.pos.repository.voucher.PosVoucherRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherApplySuccessDTO;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderVoucherCodeDTO;
import vn.com.buaansach.web.pos.util.TimelineUtil;
import vn.com.buaansach.web.pos.websocket.PosSocketService;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PosVoucherCodeService {
    private final Logger log = LoggerFactory.getLogger(PosVoucherCodeService.class);
    private final PosStoreSecurity posStoreSecurity;
    private final PosVoucherCodeRepository posVoucherCodeRepository;
    private final PosVoucherRepository posVoucherRepository;
    private final PosOrderRepository posOrderRepository;
    private final PosStoreRepository posStoreRepository;
    private final PosSocketService posSocketService;

    private boolean isVoucherCodeValid(PosVoucherCodeDTO dto) {
        if (!dto.isVoucherActivated()) throw new BadRequestException(ErrorCode.VOUCHER_DISABLED);
        if (!dto.isVoucherCodeActivated()) throw new BadRequestException(ErrorCode.VOUCHER_CODE_DISABLED);
        if (dto.getTimeCondition() != null) {
            Instant now = Instant.now();
            Instant validFrom = dto.getTimeCondition().getValidFrom();
            Instant validUntil = dto.getTimeCondition().getValidUntil();
            int cmp = now.compareTo(validFrom);
            if (cmp < 0) throw new BadRequestException(ErrorCode.VOUCHER_UNUSABLE);
            cmp = now.compareTo(validUntil);
            if (cmp > 0) throw new BadRequestException(ErrorCode.VOUCHER_EXPIRED);
        }
        if (dto.getUsageCondition() != null) {
            if (dto.getVoucherCodeUsageCount() >= dto.getUsageCondition().getMaxUsage())
                throw new BadRequestException(ErrorCode.VOUCHER_CODE_MAX_USED);
        }
        return true;
    }

    @Transactional
    public PosVoucherApplySuccessDTO applyOrderVoucherCode(PosOrderVoucherCodeDTO payload) {
        /* Không tìm thấy mã code sẽ log error nhằm mục đích phát hiện hành vi cố tình thử sai */
        PosVoucherCodeDTO voucherCodeDTO = posVoucherCodeRepository.getPosVoucherCodeDTO(payload.getVoucherCode())
                .orElseThrow(() -> {
                    log.error("Reject request from user [{}] to apply voucher code : [{}]", SecurityUtils.getCurrentUserLogin(), payload);
                    throw new NotFoundException(ErrorCode.VOUCHER_CODE_NOT_FOUND);
                });
        if (voucherCodeDTO.getVoucherCodePhone() != null && !voucherCodeDTO.getVoucherCodePhone().equals(payload.getCustomerPhone())) {
            log.error("Reject request from user [{}] to apply voucher code : [{}]", SecurityUtils.getCurrentUserLogin(), payload);
            throw new BadRequestException(ErrorCode.VOUCHER_CODE_AND_PHONE_NOT_MATCH);
        }
        /* Kiểm tra các điều kiện của voucherCode trước khi thực hiện áp dụng */
        if (isVoucherCodeValid(voucherCodeDTO)) {
            OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
            StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
            posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

            /* Tăng lượt sử dụng của mã code */
            VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(payload.getVoucherCode())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.VOUCHER_CODE_NOT_FOUND));
            voucherCodeEntity.setVoucherCodeUsageCount(voucherCodeEntity.getVoucherCodeUsageCount() + 1);
            posVoucherCodeRepository.save(voucherCodeEntity);

            /* Áp dụng khuyến mãi cho hóa đơn */
            orderEntity.setVoucherCode(payload.getVoucherCode());
            orderEntity.setOrderDiscount(voucherCodeDTO.getVoucherDiscount());
            orderEntity.setOrderDiscountType(voucherCodeDTO.getVoucherDiscountType());

            String newTimeline = TimelineUtil.appendCustomOrderStatus(
                    orderEntity.getOrderStatusTimeline(),
                    "APPLY_VOUCHER",
                    SecurityUtils.getCurrentUserLogin(),
                    payload.getVoucherCode());
            orderEntity.setOrderStatusTimeline(newTimeline);

            posOrderRepository.save(orderEntity);

            return new PosVoucherApplySuccessDTO(voucherCodeDTO);
        } else {
            log.error("Reject request from user [{}] to apply voucher code : [{}]", SecurityUtils.getCurrentUserLogin(), payload);
            throw new BadRequestException(ErrorCode.VOUCHER_CODE_INVALID);
        }
    }

    @Transactional
    public void cancelVoucherCode(PosOrderVoucherCodeDTO payload) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(orderEntity.getVoucherCode())
                .orElseThrow(() -> new NotFoundException(ErrorCode.VOUCHER_CODE_NOT_FOUND));

        /* Nếu lượt sử dụng > 0 mới thực hiện trừ số lượt đã sử dụng */
        if (voucherCodeEntity.getVoucherCodeUsageCount() > 0) {
            voucherCodeEntity.setVoucherCodeUsageCount(voucherCodeEntity.getVoucherCodeUsageCount() - 1);
            posVoucherCodeRepository.save(voucherCodeEntity);
        }

        /* Hủy các khuyến mãi đã áp dụng trên đơn */
        orderEntity.setVoucherCode(null);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderDiscount(0);
        String newTimeline = TimelineUtil.appendCustomOrderStatus(
                orderEntity.getOrderStatusTimeline(),
                "CANCEL_VOUCHER",
                SecurityUtils.getCurrentUserLogin());
        orderEntity.setOrderStatusTimeline(newTimeline);
        posOrderRepository.save(orderEntity);
    }
}
