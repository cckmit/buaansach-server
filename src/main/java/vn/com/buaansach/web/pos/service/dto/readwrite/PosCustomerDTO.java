package vn.com.buaansach.web.pos.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.entity.customer.CustomerEntity;

@Data
public class PosCustomerDTO {
    private String customerName;
    private String customerPhone;

    public PosCustomerDTO() {
    }

    public PosCustomerDTO(CustomerEntity customerEntity) {
        this.customerPhone = customerEntity.getCustomerPhone();
        this.customerName = customerEntity.getCustomerName();
    }
}
