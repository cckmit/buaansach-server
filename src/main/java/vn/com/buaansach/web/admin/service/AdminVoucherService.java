package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherTimeConditionEntity;
import vn.com.buaansach.entity.voucher.condition.VoucherUsageConditionEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.VoucherUtil;
import vn.com.buaansach.web.admin.repository.voucher.AdminVoucherCodeRepository;
import vn.com.buaansach.web.admin.repository.voucher.AdminVoucherRepository;
import vn.com.buaansach.web.admin.repository.voucher.AdminVoucherTimeConditionRepository;
import vn.com.buaansach.web.admin.repository.voucher.AdminVoucherUsageConditionRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminVoucherDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateVoucherDTO;
import vn.com.buaansach.web.admin.service.mapper.AdminVoucherMapper;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private Set<String> generateVoucherCode(int numberOfCode) {
        List<VoucherCodeEntity> existedVoucherCodes = adminVoucherCodeRepository.findAll();
        Set<String> existedCodes = existedVoucherCodes.stream().map(VoucherCodeEntity::getVoucherCode).collect(Collectors.toSet());

        Set<String> setNewCodes = new HashSet<>();
        while (setNewCodes.size() < numberOfCode) {
            String code = VoucherUtil.generateVoucherCode(VoucherUtil.VOUCHER_CODE_LENGTH).toLowerCase();
            if (!existedCodes.contains(code)) {
                setNewCodes.add(code);
            }
        }
        return setNewCodes;
    }

    @Transactional
    public AdminVoucherDTO createVoucher(AdminVoucherDTO payload) {
        VoucherEntity voucherEntity = adminVoucherMapper.dtoToEntity(payload);
        UUID voucherGuid = UUID.randomUUID();
        voucherEntity.setGuid(voucherGuid);

        Set<String> generatedCode = generateVoucherCode(payload.getNumberVoucherCode());
        List<VoucherCodeEntity> listVoucherCode = generatedCode.stream().map(code -> {
            VoucherCodeEntity voucherCodeEntity = new VoucherCodeEntity();
            voucherCodeEntity.setVoucherCode(code);
            voucherCodeEntity.setVoucherCodeActivated(false);
            voucherCodeEntity.setVoucherCodeUsageCount(0);
            voucherCodeEntity.setVoucherCodePhone(null);
            voucherCodeEntity.setVoucherGuid(voucherGuid);
            return voucherCodeEntity;
        }).collect(Collectors.toList());

        adminVoucherCodeRepository.saveAll(listVoucherCode);

        AdminVoucherDTO result = new AdminVoucherDTO(adminVoucherRepository.save(voucherEntity));

        VoucherTimeConditionEntity timeConditionEntity = payload.getTimeCondition();
        VoucherUsageConditionEntity usageConditionEntity = payload.getUsageCondition();

        if (timeConditionEntity != null && timeConditionEntity.getValidFrom() != null) {
            timeConditionEntity.setVoucherGuid(voucherGuid);
            result.setTimeCondition(adminVoucherTimeConditionRepository.save(timeConditionEntity));
        }
        if (usageConditionEntity != null && usageConditionEntity.getMaxUsage() > 0) {
            usageConditionEntity.setVoucherGuid(voucherGuid);
            result.setUsageCondition(adminVoucherUsageConditionRepository.save(usageConditionEntity));
        }
        return result;
    }

    public AdminVoucherDTO updateVoucherBasic(AdminUpdateVoucherDTO payload) {
        VoucherEntity voucherEntity = adminVoucherRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherEntity.setVoucherName(payload.getVoucherName());
        voucherEntity.setVoucherNameEng(payload.getVoucherNameEng());
        voucherEntity.setVoucherDescription(payload.getVoucherDescription());
        voucherEntity.setVoucherDescriptionEng(payload.getVoucherDescriptionEng());
        voucherEntity.setVoucherDiscount(payload.getVoucherDiscount());
        voucherEntity.setVoucherDiscountType(payload.getVoucherDiscountType());
        return new AdminVoucherDTO(adminVoucherRepository.save(voucherEntity));
    }

    public void toggleVoucher(String voucherGuid) {
        VoucherEntity voucherEntity = adminVoucherRepository.findOneByGuid(UUID.fromString(voucherGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherEntity.setVoucherActivated(!voucherEntity.isVoucherActivated());
        adminVoucherRepository.save(voucherEntity);
    }

    public List<VoucherCodeEntity> getListVoucherCodeByVoucherGuid(String voucherGuid) {
        return adminVoucherCodeRepository.findByVoucherGuid(UUID.fromString(voucherGuid));
    }
}
