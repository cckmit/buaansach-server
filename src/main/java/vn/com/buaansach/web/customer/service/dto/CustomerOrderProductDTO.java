package vn.com.buaansach.web.customer.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vn.com.buaansach.entity.OrderProductEntity;
import vn.com.buaansach.entity.ProductEntity;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class CustomerOrderProductDTO {
    private UUID guid;
    private UUID orderGuid;
    private UUID productGuid;

    private String orderProductGroup;
    private int orderProductQuantity;
    private int orderProductPrice;

    @Size(max = 255)
    private String orderProductNote;

    private String productCode;
    private String productName;
    private String productImageUrl;

    public CustomerOrderProductDTO() {
    }

    public CustomerOrderProductDTO(OrderProductEntity orderProductEntity) {
        assignProperty(orderProductEntity);
    }

    public CustomerOrderProductDTO(OrderProductEntity orderProductEntity, ProductEntity productEntity) {
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
        this.orderProductNote = orderProductEntity.getOrderProductNote();
    }

}
