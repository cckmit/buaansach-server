package vn.com.buaansach.web.guest.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GuestOrderProductMapper {
    public GuestOrderProductDTO entityToDto(OrderProductEntity entity) {
        return new GuestOrderProductDTO(entity);
    }

    public GuestOrderProductDTO entityToDto(OrderProductEntity entity, ProductEntity productEntity) {
        return new GuestOrderProductDTO(entity, productEntity);
    }

    public OrderProductEntity dtoToEntity(GuestOrderProductDTO dto) {
        if (dto == null) return null;
        OrderProductEntity entity = new OrderProductEntity();
        entity.setGuid(dto.getGuid());
        entity.setOrderGuid(dto.getOrderGuid());
        entity.setProductGuid(dto.getProductGuid());
        entity.setOrderProductGroup(dto.getOrderProductGroup());
        entity.setOrderProductQuantity(dto.getOrderProductQuantity());
        entity.setOrderProductPrice(dto.getOrderProductPrice());
        entity.setOrderProductNote(dto.getOrderProductNote());
        return entity;
    }

    public List<OrderProductEntity> listDtoToListEntity(List<GuestOrderProductDTO> dtos) {
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
