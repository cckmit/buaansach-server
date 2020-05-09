package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.service.StoreSecurityService;
import vn.com.buaansach.web.pos.repository.*;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherApplySuccessDTO;
import vn.com.buaansach.web.pos.service.dto.read.PosVoucherCodeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderVoucherCodeDTO;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class PosVoucherCodeService {
    private final StoreSecurityService storeSecurityService;
    private final PosVoucherCodeRepository posVoucherCodeRepository;
    private final PosVoucherRepository posVoucherRepository;
    private final PosVoucherInventoryService posVoucherInventoryService;
    private final PosOrderRepository posOrderRepository;
    private final PosSeatRepository posSeatRepository;
    private final PosStoreRepository posStoreRepository;
    private final PosVoucherUsageRepository posVoucherUsageRepository;

    public PosVoucherCodeService(StoreSecurityService storeSecurityService, PosVoucherCodeRepository posVoucherCodeRepository, PosVoucherRepository posVoucherRepository, PosVoucherInventoryService posVoucherInventoryService, PosOrderRepository posOrderRepository, PosSeatRepository posSeatRepository, PosStoreRepository posStoreRepository, PosVoucherUsageRepository posVoucherUsageRepository) {
        this.storeSecurityService = storeSecurityService;
        this.posVoucherCodeRepository = posVoucherCodeRepository;
        this.posVoucherRepository = posVoucherRepository;
        this.posVoucherInventoryService = posVoucherInventoryService;
        this.posOrderRepository = posOrderRepository;
        this.posSeatRepository = posSeatRepository;
        this.posStoreRepository = posStoreRepository;
        this.posVoucherUsageRepository = posVoucherUsageRepository;
    }

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
                .orElseThrow(() -> new ResourceNotFoundException("Voucher code not found " + payload.getVoucherCode()));
        if (voucherCodeDTO.getCustomerPhone() != null && !voucherCodeDTO.getCustomerPhone().equals(payload.getCustomerPhone())) {
            throw new BadRequestException("Voucher code can't be used for phone number: " + payload.getCustomerPhone());
        }
        if (isVoucherCodeValid(voucherCodeDTO, null)) {
            OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));
            StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));
            storeSecurityService.blockAccessIfNotInStore(storeEntity.getGuid());

            /* appy voucher code on order entity */
            orderEntity.setOrderVoucherCode(payload.getVoucherCode());
            orderEntity.setOrderDiscountType(voucherCodeDTO.getVoucherDiscountType());
            orderEntity.setOrderDiscount(voucherCodeDTO.getVoucherDiscount());
            posOrderRepository.save(orderEntity);

            /* increase usage count */
            VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(payload.getVoucherCode()).orElseThrow();
            posVoucherCodeRepository.increaseVoucherCodeUsageCount(voucherCodeEntity.getVoucherCode());

            return new PosVoucherApplySuccessDTO(voucherCodeDTO);
        } else {
            throw new BadRequestException("Voucher code is not valid");
        }
    }

    @Transactional
    public void cancelVoucherCode(PosOrderVoucherCodeDTO payload) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with guid: " + payload.getOrderGuid()));
        StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found in any store: " + orderEntity.getSeatGuid()));
        storeSecurityService.blockAccessIfNotInStore(storeEntity.getGuid());

        VoucherCodeEntity voucherCodeEntity = posVoucherCodeRepository.findOneByVoucherCodeForUpdate(orderEntity.getOrderVoucherCode()).orElseThrow();
        if (voucherCodeEntity.getVoucherCodeUsageCount() > 0)
            posVoucherCodeRepository.decreaseVoucherCodeUsageCount(voucherCodeEntity.getVoucherCode());

        /* appy voucher code on order entity */
        orderEntity.setOrderVoucherCode(null);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setOrderDiscount(0);
        posOrderRepository.save(orderEntity);
        /* decrease usage count */
    }

    @Transactional
    public void createVoucherForCustomerRegistration(String customerPhone) {
        VoucherEntity voucherEntity = posVoucherRepository.selectForUpdate(1L).orElseThrow();
        if (!voucherEntity.isVoucherEnable()) return;
        posVoucherRepository.increaseNumberVoucherCode(1L);
        VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
        voucherCodeEntity.setCustomerPhone(customerPhone);
        voucherCodeEntity.setVoucherCodeUsageCount(0);
        voucherCodeEntity.setVoucherCodeUsable(true);
        voucherCodeEntity.setVoucherGuid(voucherEntity.getGuid());
        voucherCodeEntity.setVoucherCode(posVoucherInventoryService.getOneVoucherCode());
        posVoucherCodeRepository.save(voucherCodeEntity);
    }


}
