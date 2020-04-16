package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.StoreUserRole;
import vn.com.buaansach.entity.enumeration.StoreUserStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bas_store_user")
@Data
public class StoreUserEntity extends AbstractAuditingEntity implements Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Column(name = "store_guid")
    private UUID storeGuid;

    @Size(max = 50)
    @Column(name = "user_login", length = 50)
    private String userLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_user_role")
    private StoreUserRole storeUserRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_user_status")
    private StoreUserStatus storeUserStatus = StoreUserStatus.WORKING;
}
