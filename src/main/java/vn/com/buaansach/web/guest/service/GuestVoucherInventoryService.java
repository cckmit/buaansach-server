package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.voucher.VoucherInventoryEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.web.guest.repository.GuestVoucherInventoryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestVoucherInventoryService {
    private final GuestVoucherInventoryRepository guestVoucherInventoryRepository;

    @Transactional
    public String getOneVoucherCode() {
        int remainCode = guestVoucherInventoryRepository.countByExportedFalse();
        if (remainCode < 1) throw new BadRequestException("Number of remain code is not enough");
        PageRequest request = PageRequest.of(0, 1, Sort.Direction.ASC, "id");
        Page<VoucherInventoryEntity> page = guestVoucherInventoryRepository.getListUnExportedVoucherInventory(request);
        if (page.getContent().size() == 0) throw new BadRequestException("Number of remain code is not enough");
        page = page.map(entity -> {
            entity.setExported(true);
            return entity;
        });
        guestVoucherInventoryRepository.saveAll(page);
        return page.getContent().get(0).getCode();
    }

    @Transactional
    public List<String> getListVoucherCode(int size) {
        int remainCode = guestVoucherInventoryRepository.countByExportedFalse();
        if (remainCode < size) throw new BadRequestException("Number of remain code is not enough");
        PageRequest request = PageRequest.of(0, size, Sort.Direction.ASC, "id");
        Page<VoucherInventoryEntity> page = guestVoucherInventoryRepository.getListUnExportedVoucherInventory(request);
        page = page.map(entity -> {
            entity.setExported(true);
            return entity;
        });
        guestVoucherInventoryRepository.saveAll(page);
        return page.getContent().stream().map(VoucherInventoryEntity::getCode).collect(Collectors.toList());
    }
}
