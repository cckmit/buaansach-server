package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store_user")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StoreUserEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_user_role")
    private StoreUserRole storeUserRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_user_status")
    private StoreUserStatus storeUserStatus = StoreUserStatus.WORKING;

    @Column(name = "store_user_area")
    private String storeUserArea;

    @Column(name = "store_user_activated")
    private boolean storeUserActivated;

    /**
     * FK
     */

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "user_guid")
    private UUID userGuid;
}
