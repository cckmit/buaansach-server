package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.web.pos.repository.PosOrderProductRepository;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.mapper.PosOrderProductMapper;

import java.util.List;

@Service
public class PosOrderProductService {
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosOrderProductMapper posOrderProductMapper;

    public PosOrderProductService(PosOrderProductRepository posOrderProductRepository, PosOrderProductMapper posOrderProductMapper) {
        this.posOrderProductRepository = posOrderProductRepository;
        this.posOrderProductMapper = posOrderProductMapper;
    }

    public List<OrderProductEntity> saveList(List<PosOrderProductDTO> dtos) {
        return posOrderProductRepository.saveAll(posOrderProductMapper.listDtoToListEntity(dtos));
    }
}
