package vn.com.buaansach.web.pos.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;

@Service
public class PosOrderMapper {
    public PosOrderDTO entityToDto(OrderEntity entity) {
        return new PosOrderDTO(entity);
    }

    public OrderEntity dtoToEntity(PosOrderDTO dto) {
        if (dto == null) return null;
        OrderEntity entity = new OrderEntity();
        entity.setGuid(dto.getGuid());
        entity.setOrderCode(dto.getOrderCode());
        entity.setOrderStatus(dto.getOrderStatus());
        entity.setOrderNote(dto.getOrderNote());
        entity.setOrderStatusTimeline(dto.getOrderStatusTimeline());
        entity.setOrderCheckinTime(dto.getOrderCheckinTime());
        entity.setCustomerPhone(dto.getCustomerPhone());
        entity.setSeatGuid(dto.getSeatGuid());
        return entity;
    }
}
