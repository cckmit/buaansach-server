package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.SaleCondition;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.entity.sale.SaleUsageEntity;
import vn.com.buaansach.entity.sale.StoreSaleEntity;
import vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.order.AdminOrderRepository;
import vn.com.buaansach.web.admin.repository.sale.AdminSaleRepository;
import vn.com.buaansach.web.admin.repository.sale.AdminSaleTimeConditionRepository;
import vn.com.buaansach.web.admin.repository.sale.AdminSaleUsageRepository;
import vn.com.buaansach.web.admin.repository.sale.AdminStoreSaleRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminSaleApplyDTO;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSaleService {
    private final AdminSaleRepository adminSaleRepository;
    private final AdminSaleTimeConditionRepository adminSaleTimeConditionRepository;
    private final AdminSaleUsageRepository adminSaleUsageRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminStoreSaleRepository adminStoreSaleRepository;
    private final AdminStoreRepository adminStoreRepository;

    private void validateTime(SaleTimeConditionEntity entity) {
        Instant start = entity.getValidFrom();
        Instant end = entity.getValidUntil();
        if (start == null || start.getNano() > end.getNano()) throw new BadRequestException(ErrorCode.INVALID_SALE_TIME_CONDITION);
    }

    @Transactional
    public AdminSaleDTO createSale(AdminSaleDTO payload) {
        UUID saleGuid = UUID.randomUUID();
        payload.setGuid(saleGuid);
        if (payload.getTimeCondition() != null) {
            validateTime(payload.getTimeCondition());
            payload.getTimeCondition().setSaleGuid(saleGuid);
            adminSaleTimeConditionRepository.save(payload.getTimeCondition());
        }
        return new AdminSaleDTO(adminSaleRepository.save(payload.toEntity()), payload.getTimeCondition());
    }

    public AdminSaleDTO getSale(UUID saleGuid) {
        return adminSaleRepository.findOneDTOByGuid(saleGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SALE_NOT_FOUND));
    }

    public List<AdminSaleDTO> getListSale() {
        return adminSaleRepository.findListDTOByGuid();
    }

    @Transactional
    public AdminSaleDTO updateSale(AdminSaleDTO payload) {
        SaleEntity existSale = adminSaleRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SALE_NOT_FOUND));
        SaleEntity updateSale = payload.toEntity();
        updateSale.setId(existSale.getId());

        AdminSaleDTO result = new AdminSaleDTO(adminSaleRepository.save(updateSale));

        SaleTimeConditionEntity existTime = adminSaleTimeConditionRepository.findOneBySaleGuid(payload.getGuid())
                .orElse(null);

        if (payload.getTimeCondition() != null) {
            validateTime(payload.getTimeCondition());
            if (existTime != null) payload.getTimeCondition().setId(existTime.getId());
            payload.getTimeCondition().setSaleGuid(payload.getGuid());
            payload.setTimeCondition(adminSaleTimeConditionRepository.save(payload.getTimeCondition()));
        } else {
            if (existTime != null) adminSaleTimeConditionRepository.delete(existTime);
        }

        return result;
    }

    @Transactional
    public void deleteSale(UUID saleGuid) {
        List<StoreEntity> list = adminStoreRepository.findByStorePrimarySaleGuid(saleGuid);
        list = list.stream().peek(item -> {
            item.setStorePrimarySaleGuid(null);
        }).collect(Collectors.toList());
        adminStoreRepository.saveAll(list);
        adminSaleTimeConditionRepository.deleteBySaleGuid(saleGuid);
        adminSaleRepository.deleteByGuid(saleGuid);
    }

    public void applySale(AdminSaleApplyDTO payload) {
        AdminSaleDTO dto = adminSaleRepository.findOneDTOByGuid(payload.getSaleGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SALE_NOT_FOUND));

        OrderEntity orderEntity = adminOrderRepository.findOneByGuid(payload.getOrderGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if (!dto.isSaleActivated()) throw new BadRequestException(ErrorCode.SALE_DISABLED);
        if (dto.getTimeCondition() != null){
            Instant now = Instant.now();
            Instant start = dto.getTimeCondition().getValidFrom();
            Instant end = dto.getTimeCondition().getValidUntil();
            if (now.toEpochMilli() < start.toEpochMilli()) throw new BadRequestException(ErrorCode.SALE_NOT_STARTED);
            if (end != null && now.toEpochMilli() > end.toEpochMilli()) throw new BadRequestException(ErrorCode.SALE_ENDED);
        }

        if (dto.getSaleConditions().contains(SaleCondition.STORE_LIMIT.name())){
            StoreEntity storeEntity = adminStoreRepository.findOneBySeatGuid(orderEntity.getSeatGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
            StoreSaleEntity storeSaleEntity = adminStoreSaleRepository.findOneByStoreGuidAndSaleGuid(storeEntity.getGuid(), payload.getSaleGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_SALE_NOT_FOUND));
            if (!storeSaleEntity.isStoreSaleActivated()) throw new BadRequestException(ErrorCode.STORE_SALE_DISABLED);
        }

        orderEntity.setOrderDiscount(dto.getSaleDiscount());
        orderEntity.setOrderDiscountType(dto.getSaleDiscountType());
        orderEntity.setSaleGuid(dto.getGuid());
        adminOrderRepository.save(orderEntity);
    }

    public void cancelSale(UUID orderGuid){
        OrderEntity orderEntity = adminOrderRepository.findOneByGuid(orderGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        orderEntity.setOrderDiscount(0);
        orderEntity.setOrderDiscountType(null);
        orderEntity.setSaleGuid(null);
        adminOrderRepository.save(orderEntity);
    }

    public void addSaleUsage(UUID saleGuid, UUID storeGuid, UUID orderGuid){
        SaleUsageEntity saleUsageEntity = new SaleUsageEntity();
        saleUsageEntity.setSaleGuid(saleGuid);
        saleUsageEntity.setOrderGuid(orderGuid);
        saleUsageEntity.setStoreGuid(storeGuid);
        adminSaleUsageRepository.save(saleUsageEntity);
    }

    public List<AdminSaleDTO> getListStoreSaleByStore(UUID storeGuid) {
        return adminSaleRepository.findListDTOByStoreGuid(storeGuid);
    }
}
