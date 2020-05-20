package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.web.guest.repository.GuestOrderProductRepository;
import vn.com.buaansach.web.guest.repository.GuestProductRepository;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;
import vn.com.buaansach.web.guest.service.mapper.GuestOrderProductMapper;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestOrderProductService {
    private final GuestOrderProductRepository guestOrderProductRepository;
    private final GuestOrderProductMapper guestOrderProductMapper;
    private final GuestProductRepository guestProductRepository;

    public List<OrderProductEntity> saveListOrderProduct(UUID orderGuid, List<GuestOrderProductDTO> dtos, String currentUser) {
        List<OrderProductEntity> list = guestOrderProductMapper.listDtoToListEntity(dtos);

        List<UUID> uuids = list.stream().map(OrderProductEntity::getProductGuid).collect(Collectors.toList());
        Map<UUID, ProductEntity> mapProduct = getMapProduct(uuids);

        String orderProductGroup = (new Date()).getTime() + "";
        list = list.stream().peek(entity -> {
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
        }).collect(Collectors.toList());
        return guestOrderProductRepository.saveAll(list);
    }

    private List<ProductEntity> getListProductIn(List<UUID> uuids) {
        return guestProductRepository.findByGuidIn(uuids);
    }

    private Map<UUID, ProductEntity> getMapProduct(List<UUID> uuids) {
        Map<UUID, ProductEntity> mapProduct = new HashMap<>();
        guestProductRepository.findByGuidIn(uuids).forEach(product -> {
            mapProduct.put(product.getGuid(), product);
        });
        return mapProduct;
    }

    public List<GuestOrderProductDTO> getOrderProduct(String orderGuid) {
        return guestOrderProductRepository.findListGuestOrderProductDTOByOrderGuid(UUID.fromString(orderGuid));
    }
}
