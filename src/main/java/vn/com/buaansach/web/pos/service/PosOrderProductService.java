package vn.com.buaansach.web.pos.service;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.admin.service.StoreUserSecurityService;
import vn.com.buaansach.web.pos.repository.PosOrderProductRepository;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductStatusChangeDTO;
import vn.com.buaansach.web.pos.service.mapper.PosOrderProductMapper;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import java.util.List;

@Service
public class PosOrderProductService {
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosOrderProductMapper posOrderProductMapper;
    private final StoreUserSecurityService storeUserSecurityService;

    public PosOrderProductService(PosOrderProductRepository posOrderProductRepository, PosOrderProductMapper posOrderProductMapper, StoreUserSecurityService storeUserSecurityService) {
        this.posOrderProductRepository = posOrderProductRepository;
        this.posOrderProductMapper = posOrderProductMapper;
        this.storeUserSecurityService = storeUserSecurityService;
    }

    public List<OrderProductEntity> saveList(List<PosOrderProductDTO> dtos) {
        return posOrderProductRepository.saveAll(posOrderProductMapper.listDtoToListEntity(dtos));
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
