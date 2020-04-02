package vn.com.buaansach.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.AreaEntity;
import vn.com.buaansach.entity.SeatEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.AreaRepository;
import vn.com.buaansach.repository.SeatRepository;
import vn.com.buaansach.service.request.CreateSeatRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final FileService fileService;
    private final AreaRepository areaRepository;

    public SeatService(SeatRepository seatRepository, FileService fileService, AreaRepository areaRepository) {
        this.seatRepository = seatRepository;
        this.fileService = fileService;
        this.areaRepository = areaRepository;
    }

    public void createListSeat(AreaEntity areaEntity, int numberOfSeats) {
        List<SeatEntity> list = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            SeatEntity seatEntity = new SeatEntity();
            UUID guid = UUID.randomUUID();
            seatEntity.setGuid(guid);
            seatEntity.setSeatName(String.valueOf(i));
            seatEntity.setSeatQrCode(fileService.generateQRCodeForSeat(guid).getUrl());
            seatEntity.setAreaId(areaEntity.getId());
            list.add(seatEntity);
        }
        seatRepository.saveAll(list);
    }

    public SeatEntity createSeat(CreateSeatRequest request) {
        AreaEntity areaEntity = areaRepository.findOneByGuid(UUID.fromString(request.getAreaGuid()))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + request.getAreaGuid()));

        SeatEntity seatEntity = new SeatEntity();
        UUID guid = UUID.randomUUID();
        seatEntity.setGuid(guid);
        seatEntity.setSeatName(request.getSeatName());
        seatEntity.setSeatQrCode(fileService.generateQRCodeForSeat(guid).getUrl());
        seatEntity.setAreaId(areaEntity.getId());
        return seatRepository.save(seatEntity);
    }

    public SeatEntity updateSeat(SeatEntity updateEntity) {
        SeatEntity currentEntity = seatRepository.findOneByGuid(updateEntity.getGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + updateEntity.getGuid()));
        currentEntity.setSeatName(updateEntity.getSeatName());
        return seatRepository.save(currentEntity);
    }

    public List<SeatEntity> getListSeatByArea(String areaGuid) {
        AreaEntity areaEntity = areaRepository.findOneByGuid(UUID.fromString(areaGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with guid: " + areaGuid));
        return seatRepository.findByAreaId(areaEntity.getId());
    }

    public void deleteSeat(String seatGuid) {
        SeatEntity seatEntity = seatRepository.findOneByGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with guid: " + seatGuid));
        fileService.deleteByUrl(seatEntity.getSeatQrCode());
        seatRepository.deleteByGuid(UUID.fromString(seatGuid));
    }

    public void deleteByAreaId(Long areaId) {
        List<SeatEntity> listSeat = seatRepository.findByAreaId(areaId);
        listSeat.forEach(seatEntity -> {
            fileService.deleteByUrl(seatEntity.getSeatQrCode());
        });
        seatRepository.deleteAll(listSeat);
    }
}
