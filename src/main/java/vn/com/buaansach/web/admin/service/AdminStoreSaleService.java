package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.sale.StoreSaleEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.sale.AdminStoreSaleRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminMakeSalePrimaryDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminStoreSaleDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStoreSaleService {
    private final AdminStoreSaleRepository adminStoreSaleRepository;
    private final AdminStoreRepository adminStoreRepository;

    public void addStoreSale(List<AdminStoreSaleDTO> payload) {
        List<StoreSaleEntity> listEntity = payload.stream().map(item -> {
            item.setGuid(UUID.randomUUID());
            return item.toEntity();
        }).collect(Collectors.toList());
        adminStoreSaleRepository.saveAll(listEntity);
    }

    public void updateStoreSale(AdminStoreSaleDTO payload) {
        StoreSaleEntity entity = adminStoreSaleRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_SALE_NOT_FOUND));
        entity.setStoreSaleActivated(payload.isStoreSaleActivated());
        adminStoreSaleRepository.save(entity);
    }

    @Transactional
    public void deleteStoreSale(UUID storeSaleGuid) {
        StoreSaleEntity entity = adminStoreSaleRepository.findOneByGuid(storeSaleGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_SALE_NOT_FOUND));
        /* clear sale guid of store entity */
        StoreEntity store = adminStoreRepository.findOneByGuid(entity.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        store.setStorePrimarySaleGuid(null);
        adminStoreRepository.save(store);

        adminStoreSaleRepository.delete(entity);
    }

    public void makePrimary(AdminMakeSalePrimaryDTO payload) {
        adminStoreSaleRepository.findOneByStoreGuidAndSaleGuid(payload.getStoreGuid(), payload.getSaleGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_SALE_NOT_FOUND));

        StoreEntity storeEntity = adminStoreRepository.findOneByGuid(payload.getStoreGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        storeEntity.setStorePrimarySaleGuid(payload.getSaleGuid());
        adminStoreRepository.save(storeEntity);
    }

    public List<AdminStoreSaleDTO> getListStoreSaleBySale(UUID saleGuid) {
        return adminStoreSaleRepository.findBySaleGuid(saleGuid).stream().map(AdminStoreSaleDTO::new).collect(Collectors.toList());
    }
}
