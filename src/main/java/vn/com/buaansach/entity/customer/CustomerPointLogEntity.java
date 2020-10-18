package vn.com.buaansach.entity.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.PointLogType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_customer_point_log")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CustomerPointLogEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "point_log_value")
    private float pointLogValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_log_type")
    private PointLogType pointLogType;

    @Column(name = "point_log_reason")
    private String pointLogReason;

    /**
     * FK
     */

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "user_guid")
    private UUID userGuid;
}
