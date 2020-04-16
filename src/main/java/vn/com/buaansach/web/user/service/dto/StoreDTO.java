package vn.com.buaansach.web.user.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.entity.enumeration.StoreStatus;

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
    private String storeCode;

    @NotBlank
    @Size(max = 100)
    private String storeName;

    @NotBlank
    @Size(max = 255)
    private String storeAddress;

    @Size(max = 255)
    private String storeImageUrl;

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @NotBlank
    @Size(max = 100)
    private String storeOwnerName;

    /* in case define multiple phone numbers */
    @NotBlank
    @Size(max = 50)
    private String storeOwnerPhone;

    @Email
    @Size(max = 100)
    private String storeOwnerEmail;

    @Size(max = 100)
    private String storeTaxCode;

    @Size(max = 20)
    private String storeOpenHour;

    @Size(max = 20)
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
