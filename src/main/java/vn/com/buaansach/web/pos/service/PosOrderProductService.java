package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.service.StoreUserSecurityService;
import vn.com.buaansach.web.pos.repository.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.PosProductRepository;
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
    private final StoreUserSecurityService storeUserSecurityService;
    private final PosProductRepository posProductRepository;

    public PosOrderProductService(PosOrderProductRepository posOrderProductRepository, PosOrderProductMapper posOrderProductMapper, StoreUserSecurityService storeUserSecurityService, PosProductRepository posProductRepository) {
        this.posOrderProductRepository = posOrderProductRepository;
        this.posOrderProductMapper = posOrderProductMapper;
        this.storeUserSecurityService = storeUserSecurityService;
        this.posProductRepository = posProductRepository;
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
                    entity.setOrderProductPrice(product.getProductPrice());
                })
                .collect(Collectors.toList());

        return posOrderProductRepository.saveAll(list);
    }

    public void serveOrderProduct(PosOrderProductStatusChangeDTO payload, String currentUser) {
        storeUserSecurityService.blockAccessIfNotInStore(payload.getStoreGuid());

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
    }

    public void cancelOrderProduct(PosOrderProductStatusChangeDTO payload, String currentUser) {
        if (payload.getOrderProductCancelReason().isEmpty()) throw new BadRequestException("Cancel Reason is required");
        storeUserSecurityService.blockAccessIfNotInStore(payload.getStoreGuid());

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
    }
}
