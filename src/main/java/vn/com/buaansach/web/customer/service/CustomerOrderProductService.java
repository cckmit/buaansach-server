package vn.com.buaansach.web.customer.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.web.customer.repository.CustomerOrderProductRepository;
import vn.com.buaansach.web.customer.repository.CustomerProductRepository;
import vn.com.buaansach.web.customer.service.dto.CustomerOrderProductDTO;
import vn.com.buaansach.web.customer.service.mapper.CustomerOrderProductMapper;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerOrderProductService {
    private final CustomerOrderProductRepository customerOrderProductRepository;
    private final CustomerOrderProductMapper customerOrderProductMapper;
    private final CustomerProductRepository customerProductRepository;

    public CustomerOrderProductService(CustomerOrderProductRepository customerOrderProductRepository, CustomerOrderProductMapper customerOrderProductMapper, CustomerProductRepository customerProductRepository) {
        this.customerOrderProductRepository = customerOrderProductRepository;
        this.customerOrderProductMapper = customerOrderProductMapper;
        this.customerProductRepository = customerProductRepository;
    }

    public List<OrderProductEntity> saveList(UUID orderGuid, List<CustomerOrderProductDTO> dtos) {
        List<OrderProductEntity> list = customerOrderProductMapper.listDtoToListEntity(dtos);

        List<UUID> uuids = list.stream().map(OrderProductEntity::getProductGuid).collect(Collectors.toList());
        Map<UUID, ProductEntity> mapProduct = getMapProduct(uuids);

        String orderProductGroup = (new Date()).getTime() + "";
        list = list.stream().peek(entity -> {
            entity.setGuid(UUID.randomUUID());
            entity.setOrderProductGroup(orderProductGroup);
            entity.setOrderProductStatus(OrderProductStatus.CREATED);
            entity.setOrderProductStatusTimeline(TimelineUtil.initOrderProductStatus(OrderProductStatus.CREATED));
            entity.setOrderGuid(orderGuid);

            ProductEntity product = mapProduct.get(entity.getProductGuid());
            entity.setOrderProductPrice(product.getProductNormalPrice());
        }).collect(Collectors.toList());
        return customerOrderProductRepository.saveAll(list);
    }

    private List<ProductEntity> getListProductIn(List<UUID> uuids) {
        return customerProductRepository.findByGuidIn(uuids);
    }

    private Map<UUID, ProductEntity> getMapProduct(List<UUID> uuids) {
        Map<UUID, ProductEntity> mapProduct = new HashMap<>();
        customerProductRepository.findByGuidIn(uuids).forEach(product -> {
            mapProduct.put(product.getGuid(), product);
        });
        return mapProduct;
    }
}
