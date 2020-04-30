package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.service.StoreUserSecurityService;
import vn.com.buaansach.web.pos.repository.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.List;
import java.util.UUID;

@Service
public class PosSeatService {
    private final PosStoreRepository posStoreRepository;
    private final PosSeatRepository posSeatRepository;
    private final StoreUserSecurityService storeUserSecurityService;

    public PosSeatService(PosStoreRepository posStoreRepository, PosSeatRepository posSeatRepository, StoreUserSecurityService storeUserSecurityService) {
        this.posStoreRepository = posStoreRepository;
        this.posSeatRepository = posSeatRepository;
        this.storeUserSecurityService = storeUserSecurityService;
    }

    public List<PosSeatDTO> getListSeatByStoreGuid(String storeGuid) {
        storeUserSecurityService.blockAccessIfNotInStore(UUID.fromString(storeGuid));
        posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        return posSeatRepository.findListPosSeatDTOByStoreGuid(UUID.fromString(storeGuid));
    }

}
