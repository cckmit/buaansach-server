package vn.com.buaansach.web.admin.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherEntity;
import vn.com.buaansach.web.admin.service.dto.AdminVoucherDTO;

@Service
public class AdminVoucherMapper {
    public VoucherEntity dtoToEntity(AdminVoucherDTO dto) {
        if (dto == null) return null;
        VoucherEntity voucherEntity = new VoucherEntity();
        voucherEntity.setGuid(dto.getGuid());
        voucherEntity.setVoucherName(dto.getVoucherName());
        voucherEntity.setVoucherDescription(dto.getVoucherDescription());
        voucherEntity.setVoucherImageUrl(dto.getVoucherImageUrl());
        voucherEntity.setVoucherDiscount(dto.getVoucherDiscount());
        voucherEntity.setVoucherDiscountType(dto.getVoucherDiscountType());
        voucherEntity.setVoucherConditions(dto.getVoucherConditions());
        voucherEntity.setVoucherEnable(dto.isVoucherEnable());
        voucherEntity.setNumberVoucherCode(dto.getNumberVoucherCode());
        return voucherEntity;
    }
}
