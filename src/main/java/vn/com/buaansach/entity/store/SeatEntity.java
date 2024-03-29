package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_seat")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SeatEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(max = 50)
    @Column(name = "seat_name", length = 50)
    private String seatName;

    @Size(max = 50)
    @Column(name = "seat_name_eng", length = 50)
    private String seatNameEng;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status")
    private SeatStatus seatStatus = SeatStatus.EMPTY;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_service_status")
    private SeatServiceStatus seatServiceStatus = SeatServiceStatus.FINISHED;

    @Column(name = "seat_locked")
    private boolean seatLocked;

    /**
     * FK
     */

    @Column(name = "order_guid")
    private UUID orderGuid;

    @Column(name = "area_guid")
    private UUID areaGuid;
}
