package vn.com.buaansach.web.customer.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.web.customer.service.dto.CustomerOrderProductDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomerOrderProductMapper {
    public CustomerOrderProductDTO entityToDto(OrderProductEntity entity) {
        return new CustomerOrderProductDTO(entity);
    }

    public CustomerOrderProductDTO entityToDto(OrderProductEntity entity, ProductEntity productEntity) {
        return new CustomerOrderProductDTO(entity, productEntity);
    }

    public OrderProductEntity dtoToEntity(CustomerOrderProductDTO dto) {
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

    public List<OrderProductEntity> listDtoToListEntity(List<CustomerOrderProductDTO> dtos) {
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
