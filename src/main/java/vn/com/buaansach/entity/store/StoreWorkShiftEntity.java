package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.AreaType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_work_shift")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StoreWorkShiftEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Column(name = "store_work_shift_name", length = 50)
    private String storeWorkShiftName;

    @Column(name = "store_work_shift_description", length = 500)
    private String storeWorkShiftDescription;

    @Column(name = "store_work_shift_start", length = 20)
    private String storeWorkShiftStart;

    @Column(name = "store_work_shift_end", length = 20)
    private String storeWorkShiftEnd;

    /**
     * FK
     */

    @Column(name = "store_guid")
    private UUID storeGuid;
}
