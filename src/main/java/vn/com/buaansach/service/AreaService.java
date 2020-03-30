package vn.com.buaansach.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.repository.AreaRepository;

import java.util.List;

@Service
public class AreaService {
    private final AreaRepository areaRepository;
    private final StoreService storeService;

    public AreaService(AreaRepository areaRepository, StoreService storeService) {
        this.areaRepository = areaRepository;
        this.storeService = storeService;
    }

    public AreaEntity create(AreaEntity entity) {
        return areaRepository.save(entity);
    }

    public AreaEntity update(AreaEntity entity) {
        return areaRepository.save(entity);
    }

    public List<AreaEntity> getListByStore(String storeGuid) {
        return null;
    }

    public void delete(String areaGuid) {
    }
}
