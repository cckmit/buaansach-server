package vn.com.buaansach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class StoreEntity extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private UUID guid;

    @Size(min = 1, max = 20)
    @Column(name = "store_code", unique = true)
    private String storeCode;

    @Size(min = 1, max = 100)
    @Column(name = "store_name")
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

    @Size(max = 100)
    @Column(name = "store_tax_code", length = 100)
    private String storeTaxCode;

    @Size(max = 20)
    @Column(name = "store_open_hour", length = 20)
    private String storeOpenHour;

    @Size(max = 20)
    @Column(name = "store_close_hour", length = 20)
    private String storeCloseHour;
}
