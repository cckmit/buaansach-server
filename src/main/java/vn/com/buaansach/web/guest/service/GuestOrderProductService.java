package vn.com.buaansach.web.guest.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.web.guest.repository.GuestOrderProductRepository;
import vn.com.buaansach.web.guest.repository.GuestProductRepository;
import vn.com.buaansach.web.guest.service.dto.readwrite.GuestOrderProductDTO;
import vn.com.buaansach.web.guest.service.mapper.GuestOrderProductMapper;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GuestOrderProductService {
    private final GuestOrderProductRepository guestOrderProductRepository;
    private final GuestOrderProductMapper guestOrderProductMapper;
    private final GuestProductRepository guestProductRepository;

    public GuestOrderProductService(GuestOrderProductRepository guestOrderProductRepository, GuestOrderProductMapper guestOrderProductMapper, GuestProductRepository guestProductRepository) {
        this.guestOrderProductRepository = guestOrderProductRepository;
        this.guestOrderProductMapper = guestOrderProductMapper;
        this.guestProductRepository = guestProductRepository;
    }

    public List<OrderProductEntity> saveList(UUID orderGuid, List<GuestOrderProductDTO> dtos) {
        List<OrderProductEntity> list = guestOrderProductMapper.listDtoToListEntity(dtos);

        List<UUID> uuids = list.stream().map(OrderProductEntity::getProductGuid).collect(Collectors.toList());
        Map<UUID, ProductEntity> mapProduct = getMapProduct(uuids);

        String orderProductGroup = (new Date()).getTime() + "";
        list = list.stream().peek(entity -> {
            entity.setGuid(UUID.randomUUID());
            entity.setOrderProductGroup(orderProductGroup);
            entity.setOrderProductStatus(OrderProductStatus.PREPARING);
            entity.setOrderProductStatusTimeline(TimelineUtil.initOrderProductStatus(OrderProductStatus.PREPARING));
            entity.setOrderGuid(orderGuid);

            ProductEntity product = mapProduct.get(entity.getProductGuid());
            entity.setOrderProductPrice(product.getProductNormalPrice());
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
}
