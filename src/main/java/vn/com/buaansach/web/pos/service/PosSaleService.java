package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderTimelineStatus;
import vn.com.buaansach.entity.enumeration.SaleCondition;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.sale.SaleUsageEntity;
import vn.com.buaansach.entity.sale.StoreSaleEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.web.pos.repository.order.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.sale.PosSaleRepository;
import vn.com.buaansach.web.pos.repository.sale.PosSaleUsageRepository;
import vn.com.buaansach.web.pos.repository.sale.PosStoreSaleRepository;
import vn.com.buaansach.web.pos.repository.store.PosStoreRepository;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosApplySaleDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosSaleService {
    private final PosSaleRepository posSaleRepository;
    private final PosStoreSaleRepository posStoreSaleRepository;
    private final PosSaleUsageRepository posSaleUsageRepository;
    private final PosOrderRepository posOrderRepository;
    private final PosStoreRepository posStoreRepository;

    public void applySale(PosApplySaleDTO payload) {
        PosSaleDTO dto = posSaleRepository.findOnePosSaleDTOByGuid(payload.getSaleGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SALE_NOT_FOUND));

        OrderEntity orderEntity = posOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if (!dto.isSaleActivated()) throw new BadRequestException(ErrorCode.SALE_DISABLED);

        if (dto.getTimeCondition() != null) {
            Instant now = Instant.now();
            Instant start = dto.getTimeCondition().getValidFrom();
            Instant end = dto.getTimeCondition().getValidUntil();
            if (now.toEpochMilli() < start.toEpochMilli()) throw new BadRequestException(ErrorCode.SALE_NOT_STARTED);
            if (end != null && now.toEpochMilli() > end.toEpochMilli())
                throw new BadRequestException(ErrorCode.SALE_ENDED);
        }

        if (dto.getSaleConditions().contains(SaleCondition.STORE_LIMIT.name())) {
            StoreEntity storeEntity = posStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
            StoreSaleEntity storeSaleEntity = posStoreSaleRepository.findOneByStoreGuidAndSaleGuid(storeEntity.getGuid(), payload.getSaleGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_SALE_NOT_FOUND));
            if (!storeSaleEntity.isStoreSaleActivated()) throw new BadRequestException(ErrorCode.STORE_SALE_DISABLED);
        }

        String newTimeline = TimelineUtil.appendOrderStatusWithMeta(
                orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.APPLY_SALE,
                SecurityUtils.getCurrentUserLogin(),
                payload.getSaleGuid().toString());
        orderEntity.setOrderStatusTimeline(newTimeline);

        orderEntity.setOrderDiscount(dto.getSaleDiscount());
        orderEntity.setOrderDiscountType(dto.getSaleDiscountType());
        orderEntity.setSaleGuid(dto.getGuid());
        posOrderRepository.save(orderEntity);
    }

    public void cancelSale(UUID orderGuid) {
        OrderEntity orderEntity = posOrderRepository.findOneByGuid(orderGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        String newTimeline = TimelineUtil.appendOrderStatus(
                orderEntity.getOrderStatusTimeline(),
                OrderTimelineStatus.CANCEL_SALE,
                SecurityUtils.getCurrentUserLogin());
        orderEntity.setOrderStatusTimeline(newTimeline);

        orderEntity.setOrderDiscount(0);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setSaleGuid(null);
        posOrderRepository.save(orderEntity);
    }

    public void addSaleUsage(UUID saleGuid, UUID storeGuid, UUID orderGuid) {
        SaleUsageEntity saleUsageEntity = new SaleUsageEntity();
        saleUsageEntity.setSaleGuid(saleGuid);
        saleUsageEntity.setOrderGuid(orderGuid);
        saleUsageEntity.setStoreGuid(storeGuid);
        posSaleUsageRepository.save(saleUsageEntity);
    }

    public List<PosSaleDTO> getListStoreSaleByStore(UUID storeGuid) {
        return posSaleRepository.findListPosSaleDTOByStoreGuid(storeGuid);
    }
}
