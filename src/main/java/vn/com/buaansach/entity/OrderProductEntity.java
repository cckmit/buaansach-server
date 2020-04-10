package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_order_product")
@Data
public class OrderProductEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private Long productId;

    private Long orderId;

    private int quantity;

    private int priceEach;
}
