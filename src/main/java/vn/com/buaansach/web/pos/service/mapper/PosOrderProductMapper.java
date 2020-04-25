package vn.com.buaansach.web.pos.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PosOrderProductMapper {
    public PosOrderProductDTO entityToDto(OrderProductEntity entity) {
        return new PosOrderProductDTO(entity);
    }

    public PosOrderProductDTO entityToDto(OrderProductEntity entity, ProductEntity productEntity) {
        return new PosOrderProductDTO(entity, productEntity);
    }

    public OrderProductEntity dtoToEntity(PosOrderProductDTO dto) {
        if (dto == null) return null;
        OrderProductEntity entity = new OrderProductEntity();
        entity.setGuid(dto.getGuid());
        entity.setOrderGuid(dto.getOrderGuid());
        entity.setProductGuid(dto.getProductGuid());
        entity.setOrderProductGroup(dto.getOrderProductGroup());
        entity.setOrderProductQuantity(dto.getOrderProductQuantity());
        entity.setOrderProductPrice(dto.getOrderProductPrice());
        entity.setOrderProductDiscount(dto.getOrderProductDiscount());
        entity.setOrderProductNote(dto.getOrderProductNote());
        entity.setOrderProductStatus(dto.getOrderProductStatus());
        entity.setOrderProductStatusTimeline(dto.getOrderProductStatusTimeline());
        entity.setOrderProductCancelReason(dto.getOrderProductCancelReason());
        return entity;
    }

    public List<OrderProductEntity> listDtoToListEntity(List<PosOrderProductDTO> dtos) {
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
