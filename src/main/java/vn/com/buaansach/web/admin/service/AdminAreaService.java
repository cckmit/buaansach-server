package vn.com.buaansach.web.admin.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.repository.AdminAreaRepository;
import vn.com.buaansach.web.admin.repository.AdminSeatRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminAreaDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateAreaDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminAreaService {
    private final AdminAreaRepository adminAreaRepository;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminSeatService adminSeatService;
    private final AdminSeatRepository adminSeatRepository;

    public AdminAreaService(AdminAreaRepository adminAreaRepository, AdminStoreRepository adminStoreRepository, AdminSeatService adminSeatService, AdminSeatRepository adminSeatRepository) {
        this.adminAreaRepository = adminAreaRepository;
        this.adminStoreRepository = adminStoreRepository;
        this.adminSeatService = adminSeatService;
        this.adminSeatRepository = adminSeatRepository;
    }

    /* used when create area with init seats */
    private List<SeatEntity> createListSeat(AreaEntity areaEntity, int numberOfSeats, String seatPrefix) {
        List<SeatEntity> listSeat = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            SeatEntity seatEntity = new SeatEntity();
            UUID guid = UUID.randomUUID();
            seatEntity.setGuid(guid);
            if (seatPrefix != null && !seatPrefix.isEmpty()) {
                seatEntity.setSeatName(seatPrefix.trim() + " " + i);
            } else {
                seatEntity.setSeatName(String.valueOf(i));
            }
            seatEntity.setAreaGuid(areaEntity.getGuid());
            listSeat.add(seatEntity);
        }
        return adminSeatRepository.saveAll(listSeat);
    }

    @Transactional
    public AdminAreaDTO createArea(AdminCreateAreaDTO request) {
        adminStoreRepository.findOneByGuid(request.getStoreGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + request.getStoreGuid()));

        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setGuid(UUID.randomUUID());
        areaEntity.setAreaName(request.getAreaName());
        areaEntity.setStoreGuid(request.getStoreGuid());
        areaEntity = adminAreaRepository.save(areaEntity);

        List<SeatEntity> listSeat = new ArrayList<>();
        if (request.getNumberOfSeats() > 0) {
            listSeat = createListSeat(areaEntity, request.getNumberOfSeats(), request.getSeatPrefix());
        }

        return new AdminAreaDTO(areaEntity, listSeat);
    }

    public List<AdminAreaDTO> getListAreaByStore(String storeGuid) {
        adminStoreRepository.findOneByGuid(UUID.fromString(storeGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with guid: " + storeGuid));

        List<AreaEntity> listArea = adminAreaRepository.findByStoreGuid(UUID.fromString(storeGuid));
        List<SeatEntity> listSeat = adminSeatRepository.findListSeatByStoreGuid(UUID.fromString(storeGuid));
        List<AdminAreaDTO> result = new ArrayList<>();
        listArea.forEach(area -> {
            AdminAreaDTO dto = new AdminAreaDTO(area, listSeat.stream()
                    .filter(seat -> seat.getAreaGuid().equals(area.getGuid()))
                    .collect(Collectors.toList())
            );
            result.add(dto);
        });
        return result;
    }

    public AdminAreaDTO updateArea(AdminAreaDTO updateEntity) {
        AreaEntity currentEntity = adminAreaRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + updateEntity.getGuid()));

        currentEntity.setAreaName(updateEntity.getAreaName());
        return new AdminAreaDTO(adminAreaRepository.save(currentEntity), updateEntity.getListSeat());
    }

    @Transactional
    public void deleteArea(String areaGuid) {
        AreaEntity areaEntity = adminAreaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));

        adminSeatRepository.deleteByAreaGuid(areaEntity.getGuid());
        adminAreaRepository.delete(areaEntity);
    }

}
