package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.internal.jline.internal.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

    @Nullable
    @Column(name = "last_order_id")
    private Long lastOrderId;

    @JsonIgnore
    @Column(name = "area_id")
    private Long areaId;
}
