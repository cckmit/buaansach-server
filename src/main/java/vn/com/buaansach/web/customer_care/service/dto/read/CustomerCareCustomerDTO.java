package vn.com.buaansach.web.customer_care.service.dto.read;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.CustomerZaloStatus;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.core.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerCareCustomerDTO extends AuditDTO {
    private UUID guid;
    private String customerCode;
    private String customerPhone;
    private String customerName;
    private Gender customerGender;
    private CustomerZaloStatus customerZaloStatus;

    public CustomerCareCustomerDTO() {
    }

    public CustomerCareCustomerDTO(CustomerEntity customerEntity) {
        this.guid = customerEntity.getGuid();
        this.customerCode = customerEntity.getCustomerCode();
        this.customerPhone = customerEntity.getCustomerPhone();
        this.customerName = customerEntity.getCustomerName();
        this.customerGender = customerEntity.getCustomerGender();
        this.customerZaloStatus = customerEntity.getCustomerZaloStatus();

        this.createdBy = customerEntity.getCreatedBy();
        this.createdDate = customerEntity.getCreatedDate();
        this.lastModifiedBy = customerEntity.getLastModifiedBy();
        this.lastModifiedDate = customerEntity.getLastModifiedDate();
    }
}
