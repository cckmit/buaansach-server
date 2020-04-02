package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_store_user")
@Data
public class StoreUserEntity extends AbstractAuditingEntity implements Serializable {
    @Id
    @JsonIgnore
    private Long id;

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Column(name = "user_login")
    private String userLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_user_role")
    private StoreUserRole storeUserRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_user_status")
    private StoreUserStatus storeUserStatus = StoreUserStatus.WORKING;
}
