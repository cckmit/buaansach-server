package vn.com.buaansach.web.customer_care.service.dto.readwrite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.customer.CustomerOrderEntity;
import vn.com.buaansach.entity.enumeration.CustomerCareStatus;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.web.user.service.dto.AuditDTO;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerCareCustomerOrderDTO extends AuditDTO {
    private UUID guid;
    private String customerPhone;
    private CustomerCareStatus customerCareStatus;
    private String customerOrderFeedback;
    private UUID orderGuid;
    private int orderCount;
    private UUID storeGuid;

    private String customerName;
    private Gender customerGender;

    private String storeCode;
    private String storeName;

    public CustomerCareCustomerOrderDTO() {
    }

    public CustomerCareCustomerOrderDTO(CustomerOrderEntity entity) {
        this.guid = entity.getGuid();
        this.customerPhone = entity.getCustomerPhone();
        this.customerCareStatus = entity.getCustomerCareStatus();
        this.customerOrderFeedback = entity.getCustomerOrderFeedback();
        this.orderGuid = entity.getOrderGuid();
        this.orderCount = entity.getOrderCount();
        this.storeGuid = entity.getStoreGuid();

        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }

    public CustomerCareCustomerOrderDTO(CustomerOrderEntity entity, CustomerEntity customerEntity, StoreEntity storeEntity) {
        this.guid = entity.getGuid();
        this.customerPhone = entity.getCustomerPhone();
        this.customerCareStatus = entity.getCustomerCareStatus();
        this.customerOrderFeedback = entity.getCustomerOrderFeedback();
        this.orderGuid = entity.getOrderGuid();
        this.orderCount = entity.getOrderCount();
        this.storeGuid = entity.getStoreGuid();

        this.customerName = customerEntity.getCustomerName();
        this.customerGender = customerEntity.getCustomerGender();
        this.storeCode = storeEntity.getStoreCode();
        this.storeName = storeEntity.getStoreName();

        this.createdBy = entity.getCreatedBy();
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedBy = entity.getLastModifiedBy();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
