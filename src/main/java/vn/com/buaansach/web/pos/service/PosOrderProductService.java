package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.PosProductRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductStatusChangeDTO;
import vn.com.buaansach.web.pos.service.mapper.PosOrderProductMapper;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PosOrderProductService {
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosOrderProductMapper posOrderProductMapper;
    private final PosStoreSecurity posStoreSecurity;
    private final PosProductRepository posProductRepository;
    private final PosOrderRepository posOrderRepository;
    private final PosSeatService posSeatService;

    public PosOrderProductService(PosOrderProductRepository posOrderProductRepository, PosOrderProductMapper posOrderProductMapper, PosStoreSecurity posStoreSecurity, PosProductRepository posProductRepository, PosOrderRepository posOrderRepository, PosSeatService posSeatService) {
        this.posOrderProductRepository = posOrderProductRepository;
        this.posOrderProductMapper = posOrderProductMapper;
        this.posStoreSecurity = posStoreSecurity;
        this.posProductRepository = posProductRepository;
        this.posOrderRepository = posOrderRepository;
        this.posSeatService = posSeatService;
    }

    private Map<UUID, ProductEntity> getMapProduct(List<UUID> uuids) {
        Map<UUID, ProductEntity> mapProduct = new HashMap<>();
        posProductRepository.findByGuidIn(uuids).forEach(product -> {
            mapProduct.put(product.getGuid(), product);
        });
        return mapProduct;
    }

    public List<OrderProductEntity> saveList(UUID orderGuid, List<PosOrderProductDTO> dtos, String currentUser) {
        List<OrderProductEntity> list = posOrderProductMapper.listDtoToListEntity(dtos);

        List<UUID> uuids = list.stream().map(OrderProductEntity::getProductGuid).collect(Collectors.toList());
        Map<UUID, ProductEntity> mapProduct = getMapProduct(uuids);

        String orderProductGroup = (new Date()).getTime() + "";
        list = list.stream()
                .peek(entity -> {
                    entity.setGuid(UUID.randomUUID());
                    entity.setOrderProductGroup(orderProductGroup);
                    entity.setOrderProductStatus(OrderProductStatus.PREPARING);
                    entity.setOrderProductStatusTimeline(TimelineUtil.initOrderProductStatus(OrderProductStatus.PREPARING, currentUser));
                    entity.setOrderGuid(orderGuid);

                    ProductEntity product = mapProduct.get(entity.getProductGuid());
                    entity.setOrderProductRootPrice(product.getProductRootPrice());
                    entity.setOrderProductPrice(product.getProductPrice());
                    entity.setOrderProductDiscount(product.getProductDiscount());
                    entity.setOrderProductSaleGuid(product.getProductSaleGuid());
                    entity.setOrderProductVoucherCode(null);
                })
                .collect(Collectors.toList());

        return posOrderProductRepository.saveAll(list);
    }

    public void serveOrderProduct(PosOrderProductStatusChangeDTO payload, String currentUser) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());

        OrderProductEntity orderProductEntity = posOrderProductRepository.findOneByGuid(payload.getOrderProductGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order Product not found with guid: " + payload.getOrderProductGuid()));

        if (orderProductEntity.getOrderProductStatus().equals(OrderProductStatus.PREPARING)) {
            orderProductEntity.setOrderProductStatus(OrderProductStatus.SERVED);
            String timeline = TimelineUtil.appendOrderProductStatus(orderProductEntity.getOrderProductStatusTimeline(),
                    OrderProductStatus.SERVED,
                    currentUser);
            orderProductEntity.setOrderProductStatusTimeline(timeline);
        }
        posOrderProductRepository.save(orderProductEntity);
        checkSeatServiceStatus(orderProductEntity.getOrderGuid());
    }

    public void cancelOrderProduct(PosOrderProductStatusChangeDTO payload, String currentUser) {
        if (payload.getOrderProductCancelReason().isEmpty()) throw new BadRequestException("Cancel Reason is required");
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());

        OrderProductEntity orderProductEntity = posOrderProductRepository.findOneByGuid(payload.getOrderProductGuid())
                .orElseThrow(() -> new ResourceNotFoundException("Order Product not found with guid: " + payload.getOrderProductGuid()));

        if (!orderProductEntity.getOrderProductStatus().toString().contains("CANCELLED")) {
            orderProductEntity.setOrderProductCancelReason(payload.getOrderProductCancelReason());
            orderProductEntity.setOrderProductStatus(OrderProductStatus.CANCELLED_BY_EMPLOYEE);
            String timeline = TimelineUtil.appendOrderProductStatus(orderProductEntity.getOrderProductStatusTimeline(),
                    OrderProductStatus.CANCELLED_BY_EMPLOYEE,
                    currentUser);
            orderProductEntity.setOrderProductStatusTimeline(timeline);
        }
        posOrderProductRepository.save(orderProductEntity);
        checkSeatServiceStatus(orderProductEntity.getOrderGuid());
    }

    private void checkSeatServiceStatus(UUID orderGuid) {
        posOrderRepository.findOneByGuid(orderGuid).ifPresent(orderEntity -> {
            List<OrderProductStatus> listStatus = new ArrayList<>();
            listStatus.add(OrderProductStatus.CREATED);
            listStatus.add(OrderProductStatus.PREPARING);
            List<OrderProductEntity> list = posOrderProductRepository.findByOrderGuidAndOrderProductStatusIn(orderGuid, listStatus);
            if (list.size() == 0) {
                posSeatService.makeSeatServiceFinished(orderEntity.getSeatGuid());
            }
        });
    }
}
