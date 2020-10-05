package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.web.guest.repository.sale.GuestSaleRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestSaleService {
    private final GuestSaleRepository guestSaleRepository;

    public SaleEntity getSale(String saleGuid) {
        return guestSaleRepository.findOneByGuidAndSaleActivated(UUID.fromString(saleGuid), true)
                .orElse(new SaleEntity());
    }
}
