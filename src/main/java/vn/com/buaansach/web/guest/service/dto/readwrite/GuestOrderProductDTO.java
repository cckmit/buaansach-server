package vn.com.buaansach.web.guest.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.entity.enumeration.DiscountType;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GuestOrderProductDTO extends AuditDTO {
    private UUID guid;
    private UUID orderProductGroup;
    private int orderProductQuantity;
    private String orderProductNote;
    private OrderProductStatus orderProductStatus;
    @JsonIgnore
    private String orderProductStatusTimeline;
    private String orderProductCancelReason;
    private int orderProductPrice;
    private int orderProductDiscount;
    private DiscountType orderProductDiscountType;

    private UUID orderGuid;
    private UUID productGuid;
    private UUID saleGuid;

    /* Additional Info */
    private String productCode;
    private String productName;
    private String productUnit;
    private String productImageUrl;
    private String productThumbnailUrl;

    public GuestOrderProductDTO(OrderProductEntity orderProductEntity) {
        assignProperty(orderProductEntity);
    }

    public GuestOrderProductDTO(OrderProductEntity orderProductEntity, ProductEntity productEntity) {
        assignProperty(orderProductEntity);
        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productUnit = productEntity.getProductUnit();
        this.productImageUrl = productEntity.getProductImageUrl();
        this.productThumbnailUrl = productEntity.getProductThumbnailUrl();
    }

    private void assignProperty(OrderProductEntity orderProductEntity) {
        this.guid = orderProductEntity.getGuid();
        this.orderProductGroup = orderProductEntity.getOrderProductGroup();
        this.orderProductQuantity = orderProductEntity.getOrderProductQuantity();
        this.orderProductNote = orderProductEntity.getOrderProductNote();
        this.orderProductStatus = orderProductEntity.getOrderProductStatus();
        this.orderProductStatusTimeline = orderProductEntity.getOrderProductStatusTimeline();
        this.orderProductCancelReason = orderProductEntity.getOrderProductCancelReason();

        this.orderProductPrice = orderProductEntity.getOrderProductPrice();
        this.orderProductDiscount = orderProductEntity.getOrderProductDiscount();

        this.orderGuid = orderProductEntity.getOrderGuid();
        this.productGuid = orderProductEntity.getProductGuid();
        this.saleGuid = orderProductEntity.getSaleGuid();

//        this.createdBy = orderProductEntity.getCreatedBy();
        this.createdDate = orderProductEntity.getCreatedDate();
//        this.lastModifiedBy = orderProductEntity.getLastModifiedBy();
//        this.lastModifiedDate = orderProductEntity.getLastModifiedDate();
    }

}
