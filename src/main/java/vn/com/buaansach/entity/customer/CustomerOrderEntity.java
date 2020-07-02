package vn.com.buaansach.entity.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.CustomerCareStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_customer_order")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerOrderEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 20)
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_care_status")
    private CustomerCareStatus customerCareStatus;

    @Size(max = 1000)
    @Column(name = "customer_order_feedback", length = 1000)
    private String customerOrderFeedback;

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "order_count")
    private int orderCount;

    @Column(name = "store_guid")
    private UUID storeGuid;
}
