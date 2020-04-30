package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.service.StoreUserSecurityService;
import vn.com.buaansach.web.pos.repository.PosAreaRepository;
import vn.com.buaansach.web.pos.repository.PosSeatRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosAreaDTO;
import vn.com.buaansach.web.pos.service.dto.read.PosSeatDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PosAreaService {
    private final PosStoreRepository posStoreRepository;
    private final PosAreaRepository posAreaRepository;
    private final PosSeatRepository posSeatRepository;
    private final StoreUserSecurityService storeUserSecurityService;

    public PosAreaService(PosStoreRepository posStoreRepository, PosAreaRepository posAreaRepository, PosSeatRepository posSeatRepository, StoreUserSecurityService storeUserSecurityService) {
        this.posStoreRepository = posStoreRepository;
        this.posAreaRepository = posAreaRepository;
        this.posSeatRepository = posSeatRepository;
        this.storeUserSecurityService = storeUserSecurityService;
    }

    public List<PosAreaDTO> getListAreaWithSeatByStoreGuid(String storeGuid) {
        storeUserSecurityService.blockAccessIfNotInStore(UUID.fromString(storeGuid));

        StoreEntity storeEntity = posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));

        List<AreaEntity> listArea = posAreaRepository.findByStoreGuid(storeEntity.getGuid());
        List<PosSeatDTO> listSeat = posSeatRepository.findListPosSeatDTOByStoreGuid(storeEntity.getGuid());
        List<PosAreaDTO> result = new ArrayList<>();
        listArea.forEach(area -> {
            PosAreaDTO dto = new PosAreaDTO(area, listSeat.stream()
                    .filter(seat -> seat.getAreaGuid().equals(area.getGuid()))
                    .collect(Collectors.toList())
            );
            result.add(dto);
        });
        return result;
    }

    public List<PosAreaDTO> getListAreaWithoutSeatByStoreGuid(String storeGuid) {
        storeUserSecurityService.blockAccessIfNotInStore(UUID.fromString(storeGuid));

        StoreEntity storeEntity = posStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));
        return posAreaRepository.findByStoreGuid(storeEntity.getGuid())
                .stream()
                .map(PosAreaDTO::new)
                .collect(Collectors.toList());
    }
}
