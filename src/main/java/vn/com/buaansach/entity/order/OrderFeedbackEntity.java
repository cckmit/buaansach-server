package vn.com.buaansach.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.ProductQualityRating;
import vn.com.buaansach.entity.enumeration.ServiceQualityRating;

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

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Size(max = 50)
    @Column(name = "rank_name", length = 50)
    private String customerName;

    @Size(max = 20)
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_quality_rating")
    private ServiceQualityRating serviceQualityRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_quality_rating")
    private ProductQualityRating productQualityRating;

    @Size(max = 500)
    @Column(name = "feedback_content", length = 500)
    private String feedbackContent;

}