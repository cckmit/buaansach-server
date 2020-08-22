package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.order.OrderEntity;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.web.pos.repository.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.PosOrderRepository;
import vn.com.buaansach.web.pos.repository.PosProductRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductServeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductStatusChangeDTO;
import vn.com.buaansach.web.pos.service.mapper.PosOrderProductMapper;
import vn.com.buaansach.web.pos.util.TimelineUtil;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosOrderProductService {
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosOrderProductMapper posOrderProductMapper;
    private final PosStoreSecurity posStoreSecurity;
    private final PosProductRepository posProductRepository;
    private final PosOrderRepository posOrderRepository;
    private final PosSeatService posSeatService;

    private Map<UUID, ProductEntity> getMapProduct(List<UUID> uuids) {
        Map<UUID, ProductEntity> mapProduct = new HashMap<>();
        posProductRepository.findByGuidIn(uuids).forEach(product -> {
            mapProduct.put(product.getGuid(), product);
        });
        return mapProduct;
    }

    /**
     * Lưu các sản phẩm cho đơn hàng
     */
    public List<OrderProductEntity> saveListOrderProduct(UUID orderProductGroup, UUID orderGuid, List<PosOrderProductDTO> dtos, String currentUser) {
        List<OrderProductEntity> list = posOrderProductMapper.listDtoToListEntity(dtos);

        /* Tạo 1 map product để sau đó lấy thông tin product dựa theo mã guid được nhanh hơn */
        List<UUID> uuids = list.stream().map(OrderProductEntity::getProductGuid).collect(Collectors.toList());
        Map<UUID, ProductEntity> mapProduct = getMapProduct(uuids);

        list = list.stream()
                .peek(entity -> {
                    entity.setGuid(UUID.randomUUID());
                    entity.setOrderProductGroup(orderProductGroup);
                    entity.setOrderProductStatus(OrderProductStatus.PREPARING);
                    entity.setOrderProductStatusTimeline(TimelineUtil.initOrderProductStatus(OrderProductStatus.PREPARING, currentUser));
                    entity.setOrderGuid(orderGuid);

                    ProductEntity product = mapProduct.get(entity.getProductGuid());

                    if (product == null) throw new ResourceNotFoundException("pos@productNotFound@" + entity.getProductGuid());
                    if (product.getProductStatus().equals(ProductStatus.STOP_TRADING)) throw new BadRequestException("pos@productStopTrading@" + product.getGuid());

                    entity.setOrderProductRootPrice(product.getProductRootPrice());
                    entity.setOrderProductPrice(product.getProductPrice());
                    entity.setOrderProductDiscount(product.getProductDiscount());
                    entity.setSaleGuid(product.getSaleGuid());
                })
                .collect(Collectors.toList());

        return posOrderProductRepository.saveAll(list);
    }

    /**
     * Đổi trạng thái một orderProduct của một đơn hàng từ PREPARING sang SERVED
     */
    public void serveOrderProduct(PosOrderProductStatusChangeDTO payload, String currentUser) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());

        OrderProductEntity orderProductEntity = posOrderProductRepository.findOneByGuid(payload.getOrderProductGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderProductNotFound@" + payload.getOrderProductGuid()));

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

    /**
     * Đổi trạng thái tất cả orderProduct của một đơn hàng từ PREPARING sang SERVED
     */
    public void serveAllOrderProduct(PosOrderProductServeDTO payload, String currentUser) {
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());
        List<OrderProductEntity> list = posOrderProductRepository.findByGuidIn(payload.getListOrderProductGuid());
        list.forEach(orderProductEntity -> {
            if (!orderProductEntity.getOrderGuid().equals(payload.getOrderGuid())) {
                /* mã đơn của sản phẩm khác với mã đơn được gửi kèm trong payload => không hợp lệ */
                throw new BadRequestException("pos@orderProductNotMatchOrder@" + payload.getOrderGuid());
            }
            if (orderProductEntity.getOrderProductStatus().equals(OrderProductStatus.PREPARING)) {
                orderProductEntity.setOrderProductStatus(OrderProductStatus.SERVED);
                String timeline = TimelineUtil.appendOrderProductStatus(
                        orderProductEntity.getOrderProductStatusTimeline(),
                        OrderProductStatus.SERVED,
                        currentUser);
                orderProductEntity.setOrderProductStatusTimeline(timeline);
            }
        });
        posOrderProductRepository.saveAll(list);
        checkSeatServiceStatus(payload.getOrderGuid());
    }

    @Transactional
    public void cancelOrderProduct(PosOrderProductStatusChangeDTO payload, String currentUser) {
        if (payload.getOrderProductCancelReason().isEmpty())
            throw new BadRequestException("pos@orderProductCancelReasonRequired@" + payload.getOrderProductGuid());
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());

        OrderProductEntity orderProductEntity = posOrderProductRepository.findOneByGuid(payload.getOrderProductGuid())
                .orElseThrow(() -> new ResourceNotFoundException("pos@orderProductNotFound@" + payload.getOrderProductGuid()));

        /* chỉ thực hiện hủy orderProduct khi trạng thái khác trạng thái CANCELLED...*/
        if (!orderProductEntity.getOrderProductStatus().toString().contains("CANCELLED")) {
            orderProductEntity.setOrderProductCancelReason(payload.getOrderProductCancelReason());
            orderProductEntity.setOrderProductStatus(OrderProductStatus.CANCELLED_BY_EMPLOYEE);
            String timeline = TimelineUtil.appendOrderProductStatus(orderProductEntity.getOrderProductStatusTimeline(),
                    OrderProductStatus.CANCELLED_BY_EMPLOYEE,
                    currentUser);
            orderProductEntity.setOrderProductStatusTimeline(timeline);
            posOrderProductRepository.save(orderProductEntity);

            /* Tính lại giá trị đơn hàng */
            OrderEntity orderEntity = posOrderRepository.findOneByGuid((orderProductEntity.getOrderGuid()))
                    .orElseThrow(() -> new ResourceNotFoundException("pos@orderNotFound@" + orderProductEntity.getOrderGuid()));
            List<PosOrderProductDTO> listPosOrderProductDTO = posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(orderProductEntity.getOrderGuid());
            int totalAmount = calculateTotalAmount(listPosOrderProductDTO);
            orderEntity.setOrderTotalAmount(totalAmount);
            posOrderRepository.save(orderEntity);

            checkSeatServiceStatus(orderProductEntity.getOrderGuid());
        }
    }

    private int calculateTotalAmount(List<PosOrderProductDTO> listPosOrderProductDTO) {
        return listPosOrderProductDTO.stream()
                .filter(dto -> !dto.getOrderProductStatus().toString().contains("CANCELLED"))
                .mapToInt(dto -> dto.getOrderProductQuantity() * (dto.getOrderProductPrice() - dto.getOrderProductDiscount())).sum();
    }

    /* Thực hiện kiểm tra trạng thái phục vụ của chỗ ngồi
    dựa trên trạng thái của các sản phẩm thuộc đơn hàng tại vị trí đó */
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

    public List<PosOrderProductDTO> getOrderProductByOrderGuid(String orderGuid) {
        return posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(UUID.fromString(orderGuid));
    }
}
