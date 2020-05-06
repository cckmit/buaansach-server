package vn.com.buaansach.web.pos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherInventoryEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.web.pos.repository.PosVoucherInventoryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PosVoucherInventoryService {
    private final PosVoucherInventoryRepository posVoucherInventoryRepository;

    public PosVoucherInventoryService(PosVoucherInventoryRepository posVoucherInventoryRepository) {
        this.posVoucherInventoryRepository = posVoucherInventoryRepository;
    }

    @Transactional
    public String getOneVoucherCode() {
        return getListVoucherCode(1).get(0);
    }

    @Transactional
    public List<String> getListVoucherCode(int size) {
        int remainCode = posVoucherInventoryRepository.countByExportedFalse();
        if (remainCode < size) throw new BadRequestException("Number of remain code is not enough");
        PageRequest request = PageRequest.of(0, 1, Sort.Direction.ASC, "id");
        Page<VoucherInventoryEntity> page = posVoucherInventoryRepository.getListUnExportedVoucherInventory(request);
        page = page.map(entity -> {
            entity.setExported(true);
            return entity;
        });
        posVoucherInventoryRepository.saveAll(page);
        return page.getContent().stream().map(VoucherInventoryEntity::getCode).collect(Collectors.toList());
    }
}
