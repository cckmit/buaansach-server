package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.VoucherInventoryType;
import vn.com.buaansach.entity.voucher.VoucherInventoryEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.util.VoucherUtil;
import vn.com.buaansach.web.admin.repository.voucher.AdminVoucherInventoryRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminVoucherInventoryStatusDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminVoucherInventoryGenerateDTO;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminVoucherInventoryService {
    private final AdminVoucherInventoryRepository adminVoucherInventoryRepository;

    @Transactional
    public void generateVoucherInventory(AdminVoucherInventoryGenerateDTO payload) {
        int length = payload.getVoucherCodeLength();
        int total = payload.getNumberOfVoucherCode();
        if (length > 30 || length < 5)
            throw new BadRequestException("Voucher code length must be in range 5-20");
        if (total > 1000000)
            throw new BadRequestException("Number of voucher is too large");

        List<VoucherInventoryEntity> existedVoucherCodes = adminVoucherInventoryRepository.findAll();
        Set<String> existedCodes = existedVoucherCodes.stream().map(VoucherInventoryEntity::getCode).collect(Collectors.toSet());

        Set<String> setNewCodes = new HashSet<>();
        while (setNewCodes.size() < payload.getNumberOfVoucherCode()) {
            String code = VoucherUtil.generateVoucherCode(length).toLowerCase();
            if (!existedCodes.contains(code)) {
                setNewCodes.add(code);
            }
        }
        List<VoucherInventoryEntity> inventoryEntities = setNewCodes.stream()
                .map(code -> new VoucherInventoryEntity(code, payload.getVoucherCodeLength(), VoucherInventoryType.AUTO_GENERATED, false))
                .collect(Collectors.toList());
        adminVoucherInventoryRepository.saveAll(inventoryEntities);
    }

    public AdminVoucherInventoryStatusDTO countRemainVoucherCode() {
        AdminVoucherInventoryStatusDTO result = new AdminVoucherInventoryStatusDTO();
        result.setTotalCode(adminVoucherInventoryRepository.countAll());
        result.setRemainCode(adminVoucherInventoryRepository.countByExportedFalse());
        return result;
    }
}
