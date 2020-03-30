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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status")
    private SeatStatus seatStatus = SeatStatus.EMPTY;

    @NotNull
    @Size(max = 255)
    @Column(name = "seat_qr_code")
    private String seatQrCode;

    @Nullable
    @Column(name = "last_order_id")
    private Long lastOrderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JsonIgnore
    @JoinColumn(name = "area_id")
    private AreaEntity area;
}
