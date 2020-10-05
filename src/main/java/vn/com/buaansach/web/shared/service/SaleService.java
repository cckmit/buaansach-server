package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderTimelineStatus;
import vn.com.buaansach.entity.enumeration.SaleCondition;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.sale.StoreSaleEntity;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.web.shared.repository.order.OrderRepository;
import vn.com.buaansach.web.shared.repository.sale.SaleRepository;
import vn.com.buaansach.web.shared.repository.sale.StoreSaleRepository;
import vn.com.buaansach.web.shared.service.dto.read.SaleDTO;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final StoreSaleRepository storeSaleRepository;
    private final OrderRepository orderRepository;

    private boolean isValidSale(SaleDTO dto, UUID storeGuid) {
        /* Sale đã bị vô hiệu hóa toàn bộ */
        if (!dto.isSaleActivated()) return false;

        if (dto.getTimeCondition() != null) {
            Instant now = Instant.now();
            Instant start = dto.getTimeCondition().getValidFrom();
            Instant end = dto.getTimeCondition().getValidUntil();
            if (now.toEpochMilli() < start.toEpochMilli()) return false;
            if (end != null && now.toEpochMilli() > end.toEpochMilli()) return false;
        }

        if (dto.getSaleConditions().contains(SaleCondition.STORE_LIMIT.name())) {
            Optional<StoreSaleEntity> optional = storeSaleRepository.findOneByStoreGuidAndSaleGuid(storeGuid, dto.getGuid());
            if (optional.isEmpty()) return false;
            /* Sale đã bị vô hiệu tại cửa hàng hay chưa */
            return optional.get().isStoreSaleActivated();
        }
        return true;
    }


    public OrderEntity autoApplySale(OrderEntity orderEntity, UUID saleGuid, UUID storeGuid) {
        SaleDTO dto = saleRepository.findOneSaleDTOByGuid(saleGuid)
                .orElse(null);
        if (dto == null) return orderEntity;

        if (!isValidSale(dto, storeGuid)) return orderEntity;

        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(
                orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.AUTO_APPLY_SALE,
                SecurityUtils.getCurrentUserLogin(),
                saleGuid.toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        orderEntity.setOrderDiscount(dto.getSaleDiscount());
        orderEntity.setOrderDiscountType(dto.getSaleDiscountType());
        orderEntity.setSaleGuid(dto.getGuid());
        return orderRepository.save(orderEntity);
    }
}
