package vn.com.buaansach.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.OrderFeedbackAction;
import vn.com.buaansach.entity.enumeration.Rating;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_order_feedback")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFeedbackEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_feedback_action")
    private OrderFeedbackAction orderFeedbackAction;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_quality_rating")
    private Rating serviceQualityRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_quality_rating")
    private Rating productQualityRating;

    @Size(max = 500)
    @Column(name = "order_feedback_content", length = 500)
    private String orderFeedbackContent;

    /**
     * FK
     */

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "store_guid")
    private UUID storeGuid;
}
