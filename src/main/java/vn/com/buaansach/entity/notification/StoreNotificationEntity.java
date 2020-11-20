package vn.com.buaansach.entity.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.StoreNotificationStatus;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bas_store_notification")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StoreNotificationEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_notification_status")
    private StoreNotificationStatus storeNotificationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_notification_type")
    private StoreNotificationType storeNotificationType;

    @Column(name = "store_notification_hidden")
    private boolean storeNotificationHidden;

    @Column(name = "store_notification_pin")
    private boolean storeNotificationPin;

    @Column(name = "first_seen_by")
    private String firstSeenBy;

    @Column(name = "first_seen_date")
    private Instant firstSeenDate;

    @Column(name = "first_hidden_by")
    private String firstHiddenBy;

    @Column(name = "first_hidden_date")
    private Instant firstHiddenDate;

    /**
     * FK
     */

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "area_guid")
    private UUID areaGuid;

    @Column(name = "seat_guid")
    private UUID seatGuid;

    @Column(name = "order_guid")
    private UUID orderGuid;
}
