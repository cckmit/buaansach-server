package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleReportParams;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosSaleReportService {
    private final PosOrderRepository posOrderRepository;
    private final PosStoreSecurity posStoreSecurity;

    public List<PosOrderDTO> getSaleReport(PosSaleReportParams payload) {
        posStoreSecurity.blockAccessIfNotOwnerOrManager(payload.getStoreGuid());
        List<OrderEntity> listOrder;
        if (payload.getUserLogin() != null && !payload.getUserLogin().isBlank()) {
            listOrder = posOrderRepository.findListOrderForReportByUser(payload.getUserLogin(), payload.getStartDate(), payload.getEndDate());
        } else {
            listOrder = posOrderRepository.findListOrderForReport(payload.getStartDate(), payload.getEndDate());
        }
        return listOrder.stream().map(PosOrderDTO::new).collect(Collectors.toList());
    }

    public List<PosOrderDTO> getCurrentUserSaleReport(PosSaleReportParams payload) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<OrderEntity> listOrder;
        listOrder = posOrderRepository.findListOrderForReportByUser(SecurityUtils.getCurrentUserLogin(), payload.getStartDate(), payload.getEndDate());
        return listOrder.stream().map(PosOrderDTO::new).collect(Collectors.toList());
    }
}
