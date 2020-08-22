package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.StorePayRequestStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bas_store_pay_request")
@Data
@EqualsAndHashCode(callSuper = true)
public class StorePayRequestEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_pay_request_status")
    private StorePayRequestStatus storePayRequestStatus;

    @Column(name = "store_pay_request_hidden")
    private boolean storePayRequestHidden;

    @Column(name = "store_pay_request_amount")
    private int storePayRequestAmount;

    @Column(name = "store_pay_request_note")
    private String storePayRequestNote;

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
