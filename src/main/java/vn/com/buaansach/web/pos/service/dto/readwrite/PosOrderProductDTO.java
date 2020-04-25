package vn.com.buaansach.web.pos.service.dto.readwrite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.ProductEntity;
import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class PosOrderProductDTO extends AuditDTO {
    private UUID guid;
    private UUID orderGuid;
    private UUID productGuid;

    @Size(max = 16)
    private String orderProductGroup;
    private int orderProductQuantity;
    private int orderProductPrice;
    private int orderProductDiscount;
    @Size(max = 255)
    private String orderProductNote;
    @Enumerated(EnumType.STRING)
    private OrderProductStatus orderProductStatus = OrderProductStatus.WAITING;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderProductStatusTimeline;
    @Size(max = 255)
    private String orderProductCancelReason;

    private String productCode;
    private String productName;
    private String productImageUrl;


    public PosOrderProductDTO() {
    }

    public PosOrderProductDTO(OrderProductEntity orderProductEntity) {
        assignProperty(orderProductEntity);
    }

    public PosOrderProductDTO(OrderProductEntity orderProductEntity, ProductEntity productEntity) {
        assignProperty(orderProductEntity);
        this.productCode = productEntity.getProductCode();
        this.productName = productEntity.getProductName();
        this.productImageUrl = productEntity.getProductImageUrl();
    }

    private void assignProperty(OrderProductEntity orderProductEntity) {
        this.guid = orderProductEntity.getGuid();
        this.orderGuid = orderProductEntity.getOrderGuid();
        this.productGuid = orderProductEntity.getOrderGuid();
        this.orderProductGroup = orderProductEntity.getOrderProductGroup();
        this.orderProductQuantity = orderProductEntity.getOrderProductQuantity();
        this.orderProductPrice = orderProductEntity.getOrderProductPrice();
        this.orderProductDiscount = orderProductEntity.getOrderProductDiscount();
        this.orderProductNote = orderProductEntity.getOrderProductNote();
        this.orderProductStatus = orderProductEntity.getOrderProductStatus();
        this.orderProductStatusTimeline = orderProductEntity.getOrderProductStatusTimeline();
        this.orderProductCancelReason = orderProductEntity.getOrderProductCancelReason();

        this.createdBy = orderProductEntity.getCreatedBy();
        this.createdDate = orderProductEntity.getCreatedDate();
        this.lastModifiedBy = orderProductEntity.getLastModifiedBy();
        this.lastModifiedDate = orderProductEntity.getLastModifiedDate();
    }
}
