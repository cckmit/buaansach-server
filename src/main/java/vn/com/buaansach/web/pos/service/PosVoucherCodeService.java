package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.repository.PosVoucherCodeRepository;
import vn.com.buaansach.web.pos.repository.PosVoucherRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherApplySuccessDTO;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderVoucherCodeDTO;
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
                .orElseThrow(() -> new ResourceNotFoundException("Voucher code not found " + voucherCode));
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
        PosVoucherCodeDTO voucherCodeDTO = posVoucherCodeRepository.getPosVoucherCodeDTO(payload.getVoucherCode())
                .orElseThrow(() -> {
                    log.error("Reject request from user [{}] to apply voucher code : {}", SecurityUtils.getCurrentUserLogin(), payload);
                    throw new ResourceNotFoundException("Voucher code not found " + payload.getVoucherCode());
                });
        if (voucherCodeDTO.getCustomerPhone() != null && !voucherCodeDTO.getCustomerPhone().equals(payload.getCustomerPhone())) {
            log.error("Reject request from user [{}] to apply voucher code : {}", SecurityUtils.getCurrentUserLogin(), payload);
            throw new BadRequestException("Voucher code can't be used for phone number: " + payload.getCustomerPhone());
        }
        if (isVoucherCodeValid(voucherCodeDTO, null)) {
            OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));
            StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));
            posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

            /* appy voucher code on order entity */
            orderEntity.setOrderVoucherCode(payload.getVoucherCode());
            orderEntity.setOrderDiscountType(voucherCodeDTO.getVoucherDiscountType());
            orderEntity.setOrderDiscount(voucherCodeDTO.getVoucherDiscount());
            posOrderRepository.save(orderEntity);

            /* increase usage count */
            VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(payload.getVoucherCode()).orElseThrow();
            voucherCodeEntity.setVoucherCodeUsageCount(voucherCodeEntity.getVoucherCodeUsageCount() + 1);
            posVoucherCodeRepository.save(voucherCodeEntity);
            return new PosVoucherApplySuccessDTO(voucherCodeDTO);
        } else {
            log.error("Reject request from user [{}] to apply voucher code : {}", SecurityUtils.getCurrentUserLogin(), payload);
            throw new BadRequestException("Voucher code is not valid");
        }
    }

    @Transactional
    public void cancelVoucherCode(PosOrderVoucherCodeDTO payload) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));
        posStoreSecurity.blockAccessIfNotInStore(storeEntity.getGuid());

        VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(orderEntity.getOrderVoucherCode())
                .orElseThrow(() -> new BadRequestException("No voucher found on order"));

        if (voucherCodeEntity.getVoucherCodeUsageCount() > 0) {
            voucherCodeEntity.setVoucherCodeUsageCount(voucherCodeEntity.getVoucherCodeUsageCount() - 1);
            posVoucherCodeRepository.save(voucherCodeEntity);
        }

        /* apply voucher code on order entity */
        orderEntity.setOrderVoucherCode(null);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderDiscount(0);
        posOrderRepository.save(orderEntity);
        /* decrease usage count */
    }

    @Transactional
    public void createVoucherForCustomerRegistration(String customerPhone) {
        VoucherEntity voucherEntity = posVoucherRepository
                .findById(Constants.DEFAULT_FIRST_REG_VOUCHER_ID)
                .orElseThrow();
        if (!voucherEntity.isVoucherEnable()) return;
        voucherEntity.setNumberVoucherCode(posVoucherCodeRepository.countNumberVoucherCodeByVoucherGuid(voucherEntity.getGuid()));
        posVoucherRepository.save(voucherEntity);
        VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
        voucherCodeEntity.setCustomerPhone(customerPhone);
        voucherCodeEntity.setVoucherCodeUsageCount(0);
        /* will be set to true later if customer has zalo id */
        voucherCodeEntity.setVoucherCodeUsable(false);
        voucherCodeEntity.setVoucherCodeSentStatus(VoucherCodeSentStatus.UNSET);
        voucherCodeEntity.setVoucherGuid(voucherEntity.getGuid());
        voucherCodeEntity.setVoucherCode(posVoucherInventoryService.getOneVoucherCode());
        posVoucherCodeRepository.save(voucherCodeEntity);
        PosSocketDTO dto = new PosSocketDTO();
        dto.setMessage("POS_CREATE_CUSTOMER");
        dto.setPayload(customerPhone);
        posSocketService.sendMessage("/topic/manager", dto);
    }
}
