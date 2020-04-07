package vn.com.buaansach.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.buaansach.entity.StoreEntity;
import vn.com.buaansach.entity.enumeration.StoreStatus;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {
    @Column(unique = true)
    private UUID guid;

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

    @NotBlank
    @Size(max = 50)
    private String storeOwnerPhone;

    @Email
    @Size(max = 100)
    private String storeOwnerEmail;

    @Size(max = 100)
    private String storeTaxCode;

    @Size(max = 500)
    private String lastUpdateReason;

    /* audit attributes */
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

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
        this.lastUpdateReason = entity.getLastUpdateReason();
        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
