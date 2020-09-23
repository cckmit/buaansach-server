package vn.com.buaansach.entity.brand;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.VersionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_version")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VersionEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Column(name = "version_name")
    private String versionName;

    @Column(name = "version_description")
    private String versionDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "version_type")
    private VersionType versionType;

    @Column(name = "version_number")
    private String versionNumber;

    @Column(name = "version_deployed")
    private boolean versionDeployed;
}
