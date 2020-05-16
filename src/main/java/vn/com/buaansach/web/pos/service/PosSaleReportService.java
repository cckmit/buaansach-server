package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleReportParams;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleReportDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosSaleReportService {
    private final PosOrderRepository posOrderRepository;
    private final PosStoreSecurity posStoreSecurity;

    public PosSaleReportDTO getSaleReport(PosSaleReportParams payload) {
        posStoreSecurity.blockAccessIfNotOwnerOrManager(payload.getStoreGuid());
        List<OrderEntity> listOrder;
        if (payload.getUserLogin() != null && !payload.getUserLogin().isBlank()) {
            listOrder = posOrderRepository.findListOrderForReportByUser(payload.getUserLogin(), payload.getStartDate(), payload.getEndDate());
        } else {
            listOrder = posOrderRepository.findListOrderForReport(payload.getStartDate(), payload.getEndDate());
        }
        return parseReportData(listOrder);
    }

    public PosSaleReportDTO getCurrentUserSaleReport(PosSaleReportParams payload) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<OrderEntity> listOrder;
        listOrder = posOrderRepository.findListOrderForReportByUser(SecurityUtils.getCurrentUserLogin(), payload.getStartDate(), payload.getEndDate());
        return parseReportData(listOrder);
    }

    private PosSaleReportDTO parseReportData(List<OrderEntity> listOrder){
        List<OrderEntity> listPurchased = listOrder.stream().filter(item -> item.getOrderStatus().equals(OrderStatus.PURCHASED)).collect(Collectors.toList());
        List<OrderEntity> listCancelled = listOrder.stream().filter(item -> item.getOrderStatus().toString().contains("CANCELLED")).collect(Collectors.toList());
        PosSaleReportDTO result = new PosSaleReportDTO();
        result.setTotalRevenue(listPurchased.stream().mapToLong(OrderEntity::getTotalAmount).sum());
        result.setTotalOrderCount(listOrder.size());
        result.setTotalPurchasedCount(listPurchased.size());
        result.setTotalCancelledCount(listCancelled.size());
        return result;
    }
}
