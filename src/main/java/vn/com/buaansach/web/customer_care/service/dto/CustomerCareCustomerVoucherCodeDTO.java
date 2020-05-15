package vn.com.buaansach.web.customer_care.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.CustomerZaloStatus;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.enumeration.VoucherCodeSentStatus;
import vn.com.buaansach.entity.voucher.VoucherCodeEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerCareCustomerVoucherCodeDTO extends AuditDTO {
    private UUID customerGuid;
    private String customerCode;
    private String customerPhone;
    private String customerEmail;
    private boolean customerActivated = true;
    private String customerName;
    private Gender customerGender;
    private Instant customerBirthday;
    private String customerAddress;
    private String customerImageUrl;
    private String customerLangKey;
    private CustomerZaloStatus customerZaloStatus;
    private String customerZaloId;
    private UUID customerRankGuid;

    private String voucherCode;
    private boolean voucherCodeUsable;
    private VoucherCodeSentStatus voucherCodeSentStatus;
    private int voucherCodeUsageCount;

    public CustomerCareCustomerVoucherCodeDTO() {
    }

    public CustomerCareCustomerVoucherCodeDTO(CustomerEntity customerEntity, VoucherCodeEntity voucherCodeEntity) {
        this.customerGuid = customerEntity.getGuid();
        this.customerCode = customerEntity.getCustomerCode();
        this.customerPhone = customerEntity.getCustomerPhone();
        this.customerEmail = customerEntity.getCustomerEmail();
        this.customerActivated = customerEntity.isCustomerActivated();
        this.customerName = customerEntity.getCustomerName();
        this.customerGender = customerEntity.getCustomerGender();
        this.customerBirthday = customerEntity.getCustomerBirthday();
        this.customerAddress = customerEntity.getCustomerAddress();
        this.customerImageUrl = customerEntity.getCustomerImageUrl();
        this.customerLangKey = customerEntity.getCustomerLangKey();
        this.customerZaloStatus = customerEntity.getCustomerZaloStatus();
        this.customerZaloId = customerEntity.getCustomerZaloId();

        this.createdDate = customerEntity.getCreatedDate();
        this.lastModifiedBy = customerEntity.getLastModifiedBy();
        this.createdBy = customerEntity.getCreatedBy();
        this.lastModifiedDate = customerEntity.getLastModifiedDate();

        this.customerRankGuid = customerEntity.getCustomerRankGuid();
        this.voucherCode = voucherCodeEntity.getVoucherCode();
        this.voucherCodeUsable = voucherCodeEntity.isVoucherCodeUsable();
        this.voucherCodeSentStatus = voucherCodeEntity.getVoucherCodeSentStatus();
        this.voucherCodeUsageCount = voucherCodeEntity.getVoucherCodeUsageCount();
    }

}
