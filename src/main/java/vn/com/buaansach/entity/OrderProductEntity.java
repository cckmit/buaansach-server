package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

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

    @Size(max = 16)
    @Column(name = "order_time", length = 16)
    private String orderTime;

    private int quantity;

    @Column(name = "price_each")
    private int priceEach;

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "product_guid")
    private UUID productGuid;
}
