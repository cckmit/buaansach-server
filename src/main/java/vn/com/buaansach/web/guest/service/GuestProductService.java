package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.web.guest.repository.common.GuestProductRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestProductDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestProductService {
    private final GuestProductRepository guestProductRepository;

    public List<GuestProductDTO> getAllProduct() {
        return guestProductRepository.findAll().stream()
                .filter(item -> !item.getProductStatus().equals(ProductStatus.STOP_TRADING) && item.isProductActivated())
                .map(GuestProductDTO::new)
                .collect(Collectors.toList());
    }
}
