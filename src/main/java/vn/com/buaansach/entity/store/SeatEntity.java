package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.SeatServiceStatus;
import vn.com.buaansach.entity.enumeration.SeatStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_seat")
@Data
@EqualsAndHashCode(callSuper = true)
public class SeatEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @NotNull
    @Size(max = 50)
    @Column(name = "seat_name")
    private String seatName;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status")
    private SeatStatus seatStatus = SeatStatus.EMPTY;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_service_status")
    private SeatServiceStatus seatServiceStatus = SeatServiceStatus.FINISHED;

    @Column(name = "current_order_guid")
    private UUID currentOrderGuid;

    @Column(name = "area_guid")
    private UUID areaGuid;
}