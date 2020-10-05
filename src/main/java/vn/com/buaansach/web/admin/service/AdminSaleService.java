package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.entity.sale.SaleEntity;
import vn.com.buaansach.entity.sale.condition.SaleTimeConditionEntity;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.sale.AdminSaleRepository;
import vn.com.buaansach.web.admin.repository.sale.AdminSaleTimeConditionRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;
import vn.com.buaansach.web.admin.service.dto.readwrite.AdminSaleDTO;
import vn.com.buaansach.web.shared.service.FileService;

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
    private final AdminStoreRepository adminStoreRepository;
    private final FileService fileService;

    private void validateTime(SaleTimeConditionEntity entity) {
        Instant start = entity.getValidFrom();
        Instant end = entity.getValidUntil();
        if (start == null || start.getNano() > end.getNano())
            throw new BadRequestException(ErrorCode.INVALID_SALE_TIME_CONDITION);
    }

    @Transactional
    public AdminSaleDTO createSale(AdminSaleDTO payload, MultipartFile image) {
        if (image != null) {
            FileEntity fileEntity = fileService.uploadImage(image, Constants.SALE_IMAGE_PATH);
            payload.setSaleImageUrl(fileEntity.getUrl());
        }
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
        return adminSaleRepository.findOneAdminSaleDTOByGuid(saleGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SALE_NOT_FOUND));
    }

    public List<AdminSaleDTO> getListSale() {
        return adminSaleRepository.findListAdminSaleDTO();
    }

    @Transactional
    public AdminSaleDTO updateSale(AdminSaleDTO payload, MultipartFile image) {
        SaleEntity existSale = adminSaleRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SALE_NOT_FOUND));
        SaleEntity updateSale = payload.toEntity();
        updateSale.setId(existSale.getId());

        if (image != null) {
            /* if update image */
            fileService.deleteByUrl(existSale.getSaleImageUrl());
            FileEntity newImage = fileService.uploadImage(image, Constants.SALE_IMAGE_PATH);
            updateSale.setSaleImageUrl(newImage.getUrl());
        } else {
            updateSale.setSaleImageUrl(existSale.getSaleImageUrl());
        }

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
        SaleEntity saleEntity = adminSaleRepository.findOneByGuid(saleGuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SALE_NOT_FOUND));
        fileService.deleteByUrl(saleEntity.getSaleImageUrl());
        adminSaleRepository.delete(saleEntity);
    }

    public List<AdminSaleDTO> getListAdminSaleDTOByStoreGuid(UUID storeGuid) {
        return adminSaleRepository.findListAdminSaleDTOByStoreGuid(storeGuid);
    }
}
