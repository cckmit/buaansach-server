package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.voucher.*;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminVoucherDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateVoucherDTO;
import vn.com.buaansach.web.admin.service.mapper.AdminVoucherMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminVoucherService {
    private final AdminVoucherRepository adminVoucherRepository;
    private final AdminVoucherTimeConditionRepository adminVoucherTimeConditionRepository;
    private final AdminVoucherUsageConditionRepository adminVoucherUsageConditionRepository;
    private final AdminVoucherCodeRepository adminVoucherCodeRepository;
    private final AdminVoucherMapper adminVoucherMapper;

    public List<AdminVoucherDTO> getListVoucher() {
        return adminVoucherRepository.findListAdminVoucherDTO();
    }

    @Transactional
    public AdminVoucherDTO createVoucher(AdminVoucherDTO payload) {
        return null;
//        int remainCode = adminVoucherInventoryRepository.countByExportedFalse();
//        if (payload.getNumberVoucherCode() > remainCode)
//            throw new BadRequestException("admin@remainCodeNotEnough@");
//
//        PageRequest request = PageRequest.of(0, payload.getNumberVoucherCode(), Sort.Direction.ASC, "id");
//        Page<VoucherInventoryEntity> page = adminVoucherInventoryRepository.getListUnExportedVoucherInventory(request);
//        page = page.map(entity -> {
//            entity.setExported(true);
//            return entity;
//        });
//        adminVoucherInventoryRepository.saveAll(page);
//
//        VoucherEntity voucherEntity = adminVoucherMapper.dtoToEntity(payload);
//        UUID voucherGuid = UUID.randomUUID();
//        voucherEntity.setGuid(voucherGuid);
//
//        List<VoucherCodeEntity> listVoucherCode = page.stream().map(entity -> {
//            VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
//            voucherCodeEntity.setVoucherCode(entity.getCode());
//            voucherCodeEntity.setVoucherCodeUsable(false);
//            voucherCodeEntity.setVoucherCodeUsageCount(0);
//            voucherCodeEntity.setVoucherCodeClaimStatus(VoucherCodeClaimStatus.UNSET);
//            voucherCodeEntity.setCustomerPhone(null);
//            voucherCodeEntity.setVoucherGuid(voucherGuid);
//            return voucherCodeEntity;
//        }).collect(Collectors.toList());
//
//        adminVoucherCodeRepository.saveAll(listVoucherCode);
//
//        AdminVoucherDTO result = new AdminVoucherDTO(adminVoucherRepository.save(voucherEntity));
//
//        VoucherTimeConditionEntity timeConditionEntity = payload.getTimeCondition();
//        VoucherUsageConditionEntity usageConditionEntity = payload.getUsageCondition();
//        VoucherStoreConditionEntity storeConditionEntity = payload.getStoreCondition();
//
//        if (timeConditionEntity != null && timeConditionEntity.getValidFrom() != null) {
//            timeConditionEntity.setVoucherGuid(voucherGuid);
//            result.setTimeCondition(adminVoucherTimeConditionRepository.save(timeConditionEntity));
//        }
//        if (usageConditionEntity != null && usageConditionEntity.getMaxUsage() > 0) {
//            usageConditionEntity.setVoucherGuid(voucherGuid);
//            result.setUsageCondition(adminVoucherUsageConditionRepository.save(usageConditionEntity));
//        }
//        if (storeConditionEntity != null && storeConditionEntity.getStoreGuid() != null) {
//            storeConditionEntity.setVoucherGuid(voucherGuid);
//            result.setStoreCondition(adminVoucherStoreConditionRepository.save(storeConditionEntity));
//        }
//        return result;
    }

    public AdminVoucherDTO updateVoucherBasic(AdminUpdateVoucherDTO payload) {
        VoucherEntity voucherEntity = adminVoucherRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException("admin@voucherNotFound@" + payload.getGuid()));
        voucherEntity.setVoucherName(payload.getVoucherName());
        voucherEntity.setVoucherDescription(payload.getVoucherDescription());
        voucherEntity.setVoucherDiscount(payload.getVoucherDiscount());
        voucherEntity.setVoucherDiscountType(payload.getVoucherDiscountType());
        return new AdminVoucherDTO(adminVoucherRepository.save(voucherEntity));
    }

    public void toggleVoucher(String voucherGuid) {
//        VoucherEntity voucherEntity = adminVoucherRepository.findOneByGuid(UUID.fromString(voucherGuid)).orElseThrow(() -> new NotFoundException("admin@voucherNotFound@" + voucherGuid));
//        voucherEntity.setVoucherEnable(!voucherEntity.isVoucherEnable());
//        adminVoucherRepository.save(voucherEntity);
    }

    public List<VoucherCodeEntity> getListVoucherCodeByVoucherGuid(String voucherGuid) {
        return adminVoucherCodeRepository.findByVoucherGuid(UUID.fromString(voucherGuid));
    }
}
