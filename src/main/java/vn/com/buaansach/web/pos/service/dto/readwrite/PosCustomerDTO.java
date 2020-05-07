package vn.com.buaansach.web.pos.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Pattern;

@Data
public class PosCustomerDTO {
    private String customerName;
    @Pattern(regexp = Constants.PHONE_REGEX)
    private String customerPhone;

    public PosCustomerDTO() {
    }

    public PosCustomerDTO(CustomerEntity customerEntity) {
        this.customerPhone = customerEntity.getCustomerPhone();
        this.customerName = customerEntity.getCustomerName();
    }
}
