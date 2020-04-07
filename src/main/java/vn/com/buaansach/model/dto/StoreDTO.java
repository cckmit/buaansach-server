package vn.com.buaansach.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.model.entity.StoreEntity;
import vn.com.buaansach.model.entity.enumeration.StoreStatus;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StoreDTO extends AuditDTO {
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
    @Column(name = "store_tax_code", length = 20)
    private String storeOpenHour;

    @Size(max = 20)
    @Column(name = "store_tax_code", length = 20)
    private String storeCloseHour;


    public StoreDTO(StoreEntity entity) {
        this.guid = entity.getGuid();
        this.storeCode = entity.getStoreCode();
        this.storeName = entity.getStoreName();
        this.storeAddress = entity.getStoreAddress();
        this.storeImageUrl = entity.getStoreImageUrl();
        this.storeStatus = entity.getStoreStatus();
        this.storeOwnerName = entity.getStoreOwnerName();
        this.storeOwnerPhone = entity.getStoreOwnerPhone();
        this.storeOwnerEmail = entity.getStoreOwnerEmail();
        this.storeTaxCode = entity.getStoreTaxCode();
        this.storeOpenHour = entity.getStoreOpenHour();
        this.storeCloseHour = entity.getStoreCloseHour();
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
