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
@Table(name = "bas_area")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AreaEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(min = 1, max = 50)
    @Column(name = "area_name", length = 50)
    private String areaName;

    @Size(min = 1, max = 50)
    @Column(name = "area_name_eng", length = 50)
    private String areaNameEng;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_type")
    private AreaType areaType;

    @Size(max = 50)
    @Column(name = "area_color", length = 50)
    private String areaColor = "#ffffff";

    @Column(name = "area_activated")
    private boolean areaActivated;

    @Column(name = "area_position")
    private int areaPosition;

    /**
     * FK
     */

    @Column(name = "store_guid")
    private UUID storeGuid;
}
