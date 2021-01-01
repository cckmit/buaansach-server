package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.TimelineUtil;
import vn.com.buaansach.web.pos.repository.common.PosProductRepository;
import vn.com.buaansach.web.pos.repository.order.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.order.PosOrderRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductServeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductStatusChangeDTO;
import vn.com.buaansach.web.pos.service.mapper.PosOrderProductMapper;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosProductService {
    private final PosProductRepository posProductRepository;

    public Map<UUID, ProductEntity> getMapProduct(List<UUID> uuids) {
        Map<UUID, ProductEntity> mapProduct = new HashMap<>();
        posProductRepository.findByGuidIn(uuids).forEach(product -> {
            mapProduct.put(product.getGuid(), product);
        });
        return mapProduct;
    }

    public Map<UUID, ProductEntity> getMapProduct() {
        Map<UUID, ProductEntity> mapProduct = new HashMap<>();
        posProductRepository.findAll().forEach(product -> {
            mapProduct.put(product.getGuid(), product);
        });
        return mapProduct;
    }


}
