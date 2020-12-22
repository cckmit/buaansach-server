package vn.com.buaansach.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.AbstractAuditingEntity;
import vn.com.buaansach.entity.enumeration.StoreStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StoreEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(min = 1, max = 20)
    @Column(name = "store_code", unique = true, length = 20)
    private String storeCode;

    @Size(min = 1, max = 100)
    @Column(name = "store_name", length = 100)
    private String storeName;

    @Size(min = 1, max = 255)
    @Column(name = "store_address")
    private String storeAddress;

    @Size(max = 255)
    @Column(name = "store_image_url")
    private String storeImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_status")
    private StoreStatus storeStatus;

    @Size(min = 1, max = 100)
    @Column(name = "store_owner_name", length = 100)
    private String storeOwnerName;

    @Size(min = 1, max = 50)
    @Column(name = "store_owner_phone", length = 50)
    private String storeOwnerPhone;

    @Email
    @Size(max = 100)
    @Column(name = "store_owner_email", length = 100)
    private String storeOwnerEmail;

    @Size(max = 50)
    @Column(name = "store_tax_code", length = 50)
    private String storeTaxCode;

    @Size(max = 255)
    @Column(name = "store_business_hours")
    private String storeBusinessHours;

    @Column(name = "store_activated")
    private boolean storeActivated;

    @Column(name = "store_reward_point_activated")
    private boolean storeRewardPointActivated;

    @Column(name = "store_primary_sale_guid")
    private UUID storePrimarySaleGuid;

    @Column(name = "store_auto_apply_sale")
    private boolean storeAutoApplySale;

    @Column(name = "store_seat_protected")
    private boolean storeSeatProtected;
}
