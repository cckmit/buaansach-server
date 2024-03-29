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
import vn.com.buaansach.web.pos.repository.order.PosOrderProductRepository;
import vn.com.buaansach.web.pos.repository.order.PosOrderRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosOrderProductDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductServeDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosOrderProductStatusChangeDTO;
import vn.com.buaansach.web.pos.service.mapper.PosOrderProductMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosOrderProductService {
    private final PosOrderProductRepository posOrderProductRepository;
    private final PosOrderProductMapper posOrderProductMapper;
    private final PosStoreSecurity posStoreSecurity;
    private final PosOrderRepository posOrderRepository;
    private final PosSeatService posSeatService;
    private final PosProductService posProductService;

    /**
     * Lưu các sản phẩm cho đơn hàng
     */
    public List<OrderProductEntity> saveListOrderProduct(UUID orderProductGroup, UUID orderGuid, List<PosOrderProductDTO> dtos, String currentUser) {
        List<OrderProductEntity> list = posOrderProductMapper.listDtoToListEntity(dtos);

        /* Tạo 1 map product để sau đó lấy thông tin product dựa theo mã guid được nhanh hơn */
        List<UUID> uuids = list.stream().map(OrderProductEntity::getProductGuid).collect(Collectors.toList());
        Map<UUID, ProductEntity> mapProduct = posProductService.getMapProduct(uuids);

        list = list.stream()
                .peek(entity -> {
                    entity.setGuid(UUID.randomUUID());
                    entity.setOrderProductGroup(orderProductGroup);
                    entity.setOrderProductStatus(OrderProductStatus.PREPARING);
                    entity.setOrderProductStatusTimeline(TimelineUtil.initOrderProductStatus(OrderProductStatus.PREPARING, currentUser));

                    ProductEntity product = mapProduct.get(entity.getProductGuid());

                    if (product == null) throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
                    if (!product.isProductActivated()) throw new BadRequestException(ErrorCode.PRODUCT_NOT_ACTIVATED);
                    if (product.getProductStatus().equals(ProductStatus.STOP_TRADING))
                        throw new BadRequestException(ErrorCode.PRODUCT_STOP_TRADING);

                    entity.setOrderProductRootPrice(product.getProductRootPrice());
                    entity.setOrderProductPrice(product.getProductPrice());
                    entity.setOrderProductDiscount(product.getProductDiscount());
                    entity.setOrderProductDiscountType(product.getProductDiscountType());
                    entity.setOrderGuid(orderGuid);
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));

        if (orderProductEntity.getOrderProductStatus().equals(OrderProductStatus.PREPARING)) {
            orderProductEntity.setOrderProductStatus(OrderProductStatus.SERVED);
            String timeline = TimelineUtil.appendOrderProductStatus(orderProductEntity.getOrderProductStatusTimeline(),
                    OrderProductStatus.SERVED,
                    currentUser);
            orderProductEntity.setOrderProductStatusTimeline(timeline);
            posOrderProductRepository.save(orderProductEntity);
            checkSeatServiceStatus(orderProductEntity.getOrderGuid());
        }
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
                throw new BadRequestException(ErrorCode.ORDER_PRODUCT_NOT_MATCH_ORDER);
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
            throw new BadRequestException(ErrorCode.ORDER_PRODUCT_CANCEL_REASON_REQUIRED);
        posStoreSecurity.blockAccessIfNotInStore(payload.getStoreGuid());

        OrderProductEntity orderProductEntity = posOrderProductRepository.findOneByGuid(payload.getOrderProductGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));

        /* chỉ thực hiện hủy orderProduct khi trạng thái khác trạng thái CANCELLED...*/
        if (!orderProductEntity.getOrderProductStatus().equals(OrderProductStatus.CANCELLED)) {
            orderProductEntity.setOrderProductCancelReason(payload.getOrderProductCancelReason());
            orderProductEntity.setOrderProductStatus(OrderProductStatus.CANCELLED);
            String timeline = TimelineUtil.appendOrderProductStatus(orderProductEntity.getOrderProductStatusTimeline(),
                    OrderProductStatus.CANCELLED,
                    currentUser);
            orderProductEntity.setOrderProductStatusTimeline(timeline);
            posOrderProductRepository.save(orderProductEntity);

            /* Tính lại giá trị đơn hàng */
            OrderEntity orderEntity = posOrderRepository.findOneByGuid((orderProductEntity.getOrderGuid()))
                    .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
            List<PosOrderProductDTO> listPosOrderProductDTO = posOrderProductRepository.findListPosOrderProductDTOByOrderGuid(orderProductEntity.getOrderGuid());
            int totalAmount = calculateTotalAmount(listPosOrderProductDTO);
            orderEntity.setOrderTotalAmount(totalAmount);
            posOrderRepository.save(orderEntity);
            checkSeatServiceStatus(orderProductEntity.getOrderGuid());
        }
    }

    private int calculateTotalAmount(List<PosOrderProductDTO> listPosOrderProductDTO) {
        return listPosOrderProductDTO.stream()
                .filter(dto -> !dto.getOrderProductStatus().equals(OrderProductStatus.CANCELLED))
                .mapToInt(dto -> {
                    int price = dto.getOrderProductPrice();
                    if (dto.getOrderProductDiscount() > 0) {
                        switch (dto.getOrderProductDiscountType()) {
                            case VALUE:
                                price = price - dto.getOrderProductDiscount();
                                break;
                            case PERCENT:
                                price = price - (price * dto.getOrderProductDiscount() / 100);
                                break;
                        }
                    }
                    return dto.getOrderProductQuantity() * price;
                }).sum();
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
