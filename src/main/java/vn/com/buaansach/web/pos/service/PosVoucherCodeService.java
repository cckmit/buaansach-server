package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.VoucherCodeClaimStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.repository.PosVoucherCodeRepository;
import vn.com.buaansach.web.pos.repository.PosVoucherRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherApplySuccessDTO;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderVoucherCodeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosUpdateVoucherCodeDTO;
import vn.com.buaansach.web.pos.util.TimelineUtil;
import vn.com.buaansach.web.pos.websocket.PosSocketService;
import vn.com.buaansach.web.pos.websocket.dto.PosSocketDTO;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosVoucherCodeService {
    private final Logger log = LoggerFactory.getLogger(PosVoucherCodeService.class);
    private final PosStoreSecurity posStoreSecurity;
    private final PosVoucherCodeRepository posVoucherCodeRepository;
    private final PosVoucherRepository posVoucherRepository;
    private final PosVoucherInventoryService posVoucherInventoryService;
    private final PosOrderRepository posOrderRepository;
    private final PosStoreRepository posStoreRepository;
    private final PosSocketService posSocketService;


    public PosVoucherCodeDTO getVoucherCodeInfo(String voucherCode) {
        return posVoucherCodeRepository.getPosVoucherCodeDTO(voucherCode)
                .orElseThrow(() -> new ResourceNotFoundException("pos@voucherCodeNotFound@" + voucherCode));
    }

    private boolean isVoucherCodeValid(PosVoucherCodeDTO dto, UUID storeGuid) {
        if (!dto.isVoucherEnable()) throw new BadRequestException("disabled");
        if (!dto.isVoucherCodeUsable()) throw new BadRequestException("disabled");
        if (dto.getTimeCondition() != null) {
            Instant now = Instant.now();
            Instant validFrom = dto.getTimeCondition().getValidFrom();
            Instant validUntil = dto.getTimeCondition().getValidUntil();
            int cmp = now.compareTo(validFrom);
            if (cmp < 0) throw new BadRequestException("invalid");
            cmp = now.compareTo(validUntil);
            if (cmp > 0) throw new BadRequestException("expired");
        }
        if (dto.getUsageCondition() != null) {
            if (dto.getVoucherCodeUsageCount() >= dto.getUsageCondition().getMaxUsage())
                throw new BadRequestException("maxUsed");
        }
        if (dto.getStoreCondition() != null) {
            if (!dto.getStoreCondition().getStoreGuid().equals(storeGuid))
                throw new BadRequestException("invalidStore");
        }
        return true;
    }

    @Transactional
    public PosVoucherApplySuccessDTO applyOrderVoucherCode(PosOrderVoucherCodeDTO payload) {
        /* Không tìm thấy mã code sẽ log error nhằm mục đích phát hiện hành vi cố tình thử sai */
        PosVoucherCodeDTO voucherCodeDTO = posVoucherCodeRepository.getPosVoucherCodeDTO(payload.getVoucherCode())
                .orElseThrow(() -> {
                    log.error("Reject request from user [{}] to apply voucher code : [{}]", SecurityUtils.getCurrentUserLogin(), payload);
                    throw new ResourceNotFoundException("pos@voucherCodeNotFound@" + payload.getVoucherCode());
                });
        if (voucherCodeDTO.getCustomerPhone() != null && !voucherCodeDTO.getCustomerPhone().equals(payload.getCustomerPhone())) {
            log.error("Reject request from user [{}] to apply voucher code : [{}]", SecurityUtils.getCurrentUserLogin(), payload);
            throw new BadRequestException("pos@voucherCodeIllegalForCustomerPhone@" + payload.getCustomerPhone());
        }
        /* Kiểm tra các điều kiện của voucherCode trước khi thực hiện áp dụng */
        if (isVoucherCodeValid(voucherCodeDTO, null)) {
            OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                    .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));
            StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                    .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + orderEntity.getSeatGuid()));
            posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

            /* Tăng lượt sử dụng của mã code */
            VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(payload.getVoucherCode())
                    .orElseThrow(() -> new BadRequestException("pos@voucherCodeNotExist@" + payload.getVoucherCode()));
            voucherCodeEntity.setVoucherCodeUsageCount(voucherCodeEntity.getVoucherCodeUsageCount() + 1);
            posVoucherCodeRepository.save(voucherCodeEntity);

            /* Áp dụng khuyến mãi cho hóa đơn */
            orderEntity.setOrderVoucherCode(payload.getVoucherCode());
            orderEntity.setOrderDiscountType(voucherCodeDTO.getVoucherDiscountType());
            orderEntity.setOrderDiscount(voucherCodeDTO.getVoucherDiscount());

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
            throw new BadRequestException("pos@voucherCodeInvalid@" + payload.getVoucherCode());
        }
    }

    @Transactional
    public void cancelVoucherCode(PosOrderVoucherCodeDTO payload) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + payload.getOrderGuid()));
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@storeNotFoundWithSeat@" + orderEntity.getSeatGuid()));
        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(orderEntity.getOrderVoucherCode())
                .orElseThrow(() -> new BadRequestException("pos@voucherCodeNotExist@" + orderEntity.getOrderVoucherCode()));

        /* Nếu lượt sử dụng > 0 mới thực hiện trừ số lượt đã sử dụng */
        if (voucherCodeEntity.getVoucherCodeUsageCount() > 0) {
            voucherCodeEntity.setVoucherCodeUsageCount(voucherCodeEntity.getVoucherCodeUsageCount() - 1);
            posVoucherCodeRepository.save(voucherCodeEntity);
        }

        /* Hủy các khuyến mãi đã áp dụng trên đơn */
        orderEntity.setOrderVoucherCode(null);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderDiscount(0);
        String newTimeline = TimelineUtil.appendCustomOrderStatus(
                orderEntity.getOrderStatusTimeline(),
                "CANCEL_VOUCHER",
                SecurityUtils.getCurrentUserLogin());
        orderEntity.setOrderStatusTimeline(newTimeline);
        posOrderRepository.save(orderEntity);
    }

    /**
     * Tạo voucher code cho SĐT mới, mã sẽ được lấy từ kho
     */
//    @Transactional
//    public void createVoucherForCustomerRegistration(String customerPhone) {
//        VoucherEntity voucherEntity = posVoucherRepository
//                .findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID)
//                .orElseThrow();
//        voucherEntity.setNumberVoucherCode(posVoucherCodeRepository.countNumberVoucherCodeByVoucherGuid(voucherEntity.getGuid()) + 1);
//        posVoucherRepository.save(voucherEntity);
//
//        VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
//        voucherCodeEntity.setCustomerPhone(customerPhone);
//        voucherCodeEntity.setVoucherCodeUsageCount(0);
//        /* Bộ phân CSKH sẽ kiểm tra SĐT đã đăng ký Zalo hay chưa và đổi trạng thái usable sang true */
//        voucherCodeEntity.setVoucherCodeUsable(false);
//        voucherCodeEntity.setVoucherCodeClaimStatus(VoucherCodeClaimStatus.UNSET);
//        voucherCodeEntity.setVoucherGuid(voucherEntity.getGuid());
//        voucherCodeEntity.setVoucherCode(posVoucherInventoryService.getOneVoucherCode());
//        posVoucherCodeRepository.save(voucherCodeEntity);
//
//        /* Gửi thông báo tới bộ phận CSKH */
//        PosSocketDTO dto = new PosSocketDTO();
//        dto.setMessage(WebSocketConstants.POS_CREATE_CUSTOMER);
//        dto.setPayload(customerPhone);
//        posSocketService.sendMessage(WebSocketConstants.TOPIC_CUSTOMER_CARE_TRACKER, dto);
//    }
    @Transactional
    public void createVoucherForCustomerRegistration(String customerPhone, String voucherCode) {

        VoucherEntity voucherEntity = posVoucherRepository
                .findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID)
                .orElseThrow();
        voucherEntity.setNumberVoucherCode(posVoucherCodeRepository.countNumberVoucherCodeByVoucherGuid(voucherEntity.getGuid()) + 1);
        posVoucherRepository.save(voucherEntity);

        VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
        voucherCodeEntity.setCustomerPhone(customerPhone);
        voucherCodeEntity.setVoucherCodeUsageCount(0);
        /* Bộ phân CSKH sẽ kiểm tra SĐT đã đăng ký Zalo hay chưa và đổi trạng thái usable sang true */
        voucherCodeEntity.setVoucherCodeUsable(false);
        voucherCodeEntity.setVoucherCodeClaimStatus(VoucherCodeClaimStatus.UNSET);
        voucherCodeEntity.setVoucherGuid(voucherEntity.getGuid());

        posVoucherInventoryService.insertCode(voucherCode);
        voucherCodeEntity.setVoucherCode(voucherCode);
        posVoucherCodeRepository.save(voucherCodeEntity);

        /* Gửi thông báo tới bộ phận CSKH */
        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage(WebSocketConstants.POS_CREATE_CUSTOMER);
        dto.setPayload(customerPhone);
        posSocketService.sendMessage(WebSocketConstants.TOPIC_CUSTOMER_CARE_TRACKER, dto);
    }

    public void updateFirstRegVoucherCode(PosUpdateVoucherCodeDTO payload) {
        VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCode(payload.getVoucherCode())
                .orElseThrow(() -> new ResourceNotFoundException("pos@voucherCodeNotFound@" + payload.getVoucherCode()));
        voucherCodeEntity.setVoucherCodeClaimStatus(payload.getVoucherCodeClaimStatus());
        if (payload.getVoucherCodeClaimStatus().equals(VoucherCodeClaimStatus.CLAIMED)) {
            voucherCodeEntity.setVoucherCodeUsable(true);
        } else {
            voucherCodeEntity.setVoucherCodeUsable(false);
        }
        posVoucherCodeRepository.save(voucherCodeEntity);
    }
}
