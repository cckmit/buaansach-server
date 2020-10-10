package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.entity.sale.StoreSaleEntity;
import vn.com.buaansach.web.guest.repository.sale.GuestSaleRepository;
import vn.com.buaansach.web.guest.repository.sale.GuestStoreSaleRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestSaleService {
    private final GuestSaleRepository guestSaleRepository;
    private final GuestStoreSaleRepository guestStoreSaleRepository;

    public SaleEntity getSale(String saleGuid) {
        return guestSaleRepository.findOneByGuidAndSaleActivated(UUID.fromString(saleGuid), true)
                .orElse(new SaleEntity());
    }

    public SaleEntity getSaleWithStore(String saleGuid, String storeGuid) {
        StoreSaleEntity storeSaleEntity = guestStoreSaleRepository.findOneByStoreGuidAndSaleGuid(UUID.fromString(storeGuid), UUID.fromString(saleGuid))
                .orElse(null);
        if (storeSaleEntity == null || !storeSaleEntity.isStoreSaleActivated()) return new SaleEntity();
        return guestSaleRepository.findOneByGuidAndSaleActivated(UUID.fromString(saleGuid), true)
                .orElse(new SaleEntity());
    }
}
