package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.enumeration.StoreStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "bas_store")
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @NotBlank
    @Size(max = 20)
    @Column(name = "store_code", unique = true)
    private String storeCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "store_name")
    private String storeName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "store_address")
    private String storeAddress;

    @Size(max = 255)
    @Column(name = "store_image_url")
    private String storeImageUrl;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "store_status")
    private StoreStatus storeStatus;

    @NotBlank
    @Size(max = 100)
    @Column(name = "store_owner_name", length = 100)
    private String storeOwnerName;

    /* in case define multiple phone numbers */
    @NotBlank
    @Size(max = 50)
    @Column(name = "store_owner_phone")
    private String storeOwnerPhone;

    @Email
    @Size(max = 100)
    @Column(name = "store_owner_email")
    private String storeOwnerEmail;

    @Size(max = 100)
    @Column(name = "store_tax_code", length = 100)
    private String storeTaxCode;

    @Size(max = 500)
    @Column(name = "last_update_reason")
    private String lastUpdateReason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_owner_login", referencedColumnName = "login")
    @JsonIgnore
    private UserEntity storeOwnerUser;
}
