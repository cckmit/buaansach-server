package vn.com.buaansach.web.guest.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.ProductStatus;
import vn.com.buaansach.entity.order.OrderProductEntity;
import vn.com.buaansach.entity.common.ProductEntity;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class GuestOrderProductDTO extends AuditDTO {
    private UUID guid;
    private UUID orderGuid;
    private UUID productGuid;

    private UUID orderProductGroup;
    private int orderProductQuantity;
    @JsonIgnore
    private int orderProductRootPrice;
    private int orderProductPrice;
    @Size(max = 255)
    private String orderProductNote;
    private OrderProductStatus orderProductStatus;
    @JsonIgnore
    private String orderProductStatusTimeline;
    @Size(max = 255)
    private String orderProductCancelReason;
    private int orderProductDiscount;
    @JsonIgnore
    private UUID orderProductSaleGuid;
    @Size(max = 20)
    @JsonIgnore
    private String orderProductVoucherCode;

    private String productCode;
    private String productName;
    private String productUnit;
    private String productImageUrl;

    public GuestOrderProductDTO() {
    }

    public GuestOrderProductDTO(OrderProductEntity orderProductEntity) {
        assignProperty(orderProductEntity);
    }

    public GuestOrderProductDTO(OrderProductEntity orderProductEntity, ProductEntity productEntity) {
        assignProperty(orderProductEntity);
        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productUnit = productEntity.getProductUnit();
        this.productImageUrl = productEntity.getProductImageUrl();
    }

    private void assignProperty(OrderProductEntity orderProductEntity) {
        this.guid = orderProductEntity.getGuid();
        this.orderGuid = orderProductEntity.getOrderGuid();
        this.productGuid = orderProductEntity.getProductGuid();
        this.orderProductGroup = orderProductEntity.getOrderProductGroup();
        this.orderProductQuantity = orderProductEntity.getOrderProductQuantity();
        this.orderProductRootPrice = orderProductEntity.getOrderProductRootPrice();
        this.orderProductPrice = orderProductEntity.getOrderProductPrice();
        this.orderProductNote = orderProductEntity.getOrderProductNote();
        this.orderProductStatus = orderProductEntity.getOrderProductStatus();
        this.orderProductStatusTimeline = orderProductEntity.getOrderProductStatusTimeline();
        this.orderProductCancelReason = orderProductEntity.getOrderProductCancelReason();
        this.orderProductDiscount = orderProductEntity.getOrderProductDiscount();
        this.orderProductSaleGuid = orderProductEntity.getOrderProductSaleGuid();
        this.orderProductVoucherCode = orderProductEntity.getOrderProductVoucherCode();

        this.createdDate = orderProductEntity.getCreatedDate();
    }

}
