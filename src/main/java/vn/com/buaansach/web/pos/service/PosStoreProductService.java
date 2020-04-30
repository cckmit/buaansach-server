package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.web.pos.repository.PosStoreProductRepository;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreProductDTO;

import java.util.List;
import java.util.UUID;

@Service
public class PosStoreProductService {
    private final PosStoreProductRepository posStoreProductRepository;

    public PosStoreProductService(PosStoreProductRepository posStoreProductRepository) {
        this.posStoreProductRepository = posStoreProductRepository;
    }

    public List<PosStoreProductDTO> getListProductByStoreGuid(String storeGuid) {
        return posStoreProductRepository.findListPosStoreProductDTO(UUID.fromString(storeGuid));
    }
}
