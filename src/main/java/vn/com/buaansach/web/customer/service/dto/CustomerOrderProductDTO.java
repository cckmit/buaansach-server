package vn.com.buaansach.web.customer.service.dto;

import lombok.Data;

@Data
public class CustomerOrderProductDTO {
    private Long productId;

    private Long orderId;

    private int quantity;

    private int priceEach;
}
