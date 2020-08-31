package vn.com.buaansach.entity.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_pay_request_notification")
@Data
@EqualsAndHashCode(callSuper = true)
public class StorePayRequestNotificationEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "store_pay_request_amount")
    private int storePayRequestAmount;

    @Column(name = "store_pay_request_note")
    private String storePayRequestNote;

    @Column(name = "number_of_extra_seat")
    private int numberOfExtraSeat;

    @Size(max = 500)
    @Column(name = "list_extra_seat", length = 500)
    private String listExtraSeat;

    @Size(max = 500)
    @Column(name = "list_extra_order", length = 500)
    private String listExtraOrder;

    /**
     * FK
     */

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "store_notification_guid")
    private UUID storeNotificationGuid;
}
