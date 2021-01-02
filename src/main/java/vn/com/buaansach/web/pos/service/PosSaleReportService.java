package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.OrderStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.repository.common.PosProductIngredientRepository;
import vn.com.buaansach.web.pos.repository.order.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.order.PosOrderRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.read.PosOrderProductReportDTO;
import vn.com.buaansach.web.pos.service.dto.read.PosSaleReportParams;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderDTO;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosSaleReportService {
    private final PosOrderRepository posOrderRepository;
    private final PosStoreSecurity posStoreSecurity;
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosProductService posProductService;
    private final PosProductIngredientRepository posProductIngredientRepository;

    public List<PosOrderDTO> getSaleReport(PosSaleReportParams payload) {
        posStoreSecurity.blockAccessIfNotOwnerOrManager(payload.getStoreGuid());
        List<OrderEntity> listOrder;
        if (payload.getUserLogin() != null && !payload.getUserLogin().isBlank()) {
            listOrder = posOrderRepository.findListOrderForReportByUser(payload.getUserLogin(), payload.getStartDate(), payload.getEndDate(), payload.getStoreGuid());
        } else {
            listOrder = posOrderRepository.findListOrderForReport(payload.getStartDate(), payload.getEndDate(), payload.getStoreGuid());
        }
        return listOrder.stream().map(PosOrderDTO::new).collect(Collectors.toList());
    }

    public List<PosOrderDTO> getCurrentUserSaleReport(PosSaleReportParams payload) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<OrderEntity> listOrder;
        listOrder = posOrderRepository.findListOrderForReportByUser(SecurityUtils.getCurrentUserLogin(), payload.getStartDate(), payload.getEndDate(), payload.getStoreGuid());
        return listOrder.stream().map(PosOrderDTO::new).collect(Collectors.toList());
    }

    public List<PosOrderProductReportDTO> getOrderProductReport(PosSaleReportParams payload) {
        posStoreSecurity.blockAccessIfNotOwnerOrManager(payload.getStoreGuid());

        // Lay ra list order theo user va khoang thoi gian
        List<OrderEntity> listOrder;
        if (payload.getUserLogin() != null && !payload.getUserLogin().isBlank()) {
            listOrder = posOrderRepository.findListOrderForReportByUser(payload.getUserLogin(), payload.getStartDate(), payload.getEndDate(), payload.getStoreGuid());
        } else {
            listOrder = posOrderRepository.findListOrderForReport(payload.getStartDate(), payload.getEndDate(), payload.getStoreGuid());
        }
        List<UUID> listOrderGuid = listOrder.stream()
                .filter(item-> item.getOrderStatus().equals(OrderStatus.PURCHASED))
                .map(OrderEntity::getGuid)
                .collect(Collectors.toList());

        // Lay ra list order product theo list order ben tren
        List<OrderProductEntity> listOrderProduct = posOrderProductRepository.findByOrderGuidIn(listOrderGuid);

        Map<UUID, ProductEntity> mapProduct = posProductService.getMapProduct(listOrderProduct.stream()
                .map(OrderProductEntity::getProductGuid)
                .collect(Collectors.toList()));

        // Phan loai va nhom order product trung lap
        Map<UUID, List<OrderProductEntity>> mapOrderProduct = new HashMap<>();
        listOrderProduct.forEach(item -> {
            List<OrderProductEntity> list = mapOrderProduct.get(item.getProductGuid());
            if (list == null) list = new ArrayList<>();
            list.add(item);
            mapOrderProduct.put(item.getProductGuid(), list);
        });

        Map<UUID, PosOrderProductReportDTO> mapResult = new HashMap<>();
        for (Map.Entry<UUID, List<OrderProductEntity>> entry : mapOrderProduct.entrySet()) {
            List<OrderProductEntity> list = entry.getValue();
            List<OrderProductEntity> listSold = list.stream()
                    .filter(item -> item.getOrderProductStatus().equals(OrderProductStatus.SERVED))
                    .collect(Collectors.toList());

            List<OrderProductEntity> listCancelled = list.stream()
                    .filter(item -> item.getOrderProductStatus().equals(OrderProductStatus.CANCELLED))
                    .collect(Collectors.toList());

            PosOrderProductReportDTO element = new PosOrderProductReportDTO();
            element.setProductGuid(entry.getKey());
            element.setProduct(mapProduct.get(entry.getKey()));
            element.setNumberSold(listSold.size());
            element.setNumberCancelled(listCancelled.size());
            element.setNumberPending(list.size() - listSold.size() - listCancelled.size());
            element.setListProductIngredient(posProductIngredientRepository.findByProductGuid(entry.getKey()));
            mapResult.put(entry.getKey(), element);
        }

        List<PosOrderProductReportDTO> result = new ArrayList<>();
        result = new ArrayList<>(mapResult.values());
        result.sort((o1, o2) -> {
            int p1 = o1.getProduct().getProductPosition();
            int p2 = o2.getProduct().getProductPosition();
            if (p1 == p2) return 0;
            return p1 > p2 ? 1 : -1;
        });

        return result;
    }
}
