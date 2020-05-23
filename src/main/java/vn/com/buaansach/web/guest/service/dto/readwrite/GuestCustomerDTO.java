package vn.com.buaansach.web.guest.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Pattern;

@Data
public class GuestCustomerDTO {
    private String customerName;

    private Gender customerGender;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String customerPhone;

    public GuestCustomerDTO() {
    }

    public GuestCustomerDTO(CustomerEntity customerEntity) {
        this.customerPhone = customerEntity.getCustomerPhone();
        this.customerName = customerEntity.getCustomerName();
        this.customerGender = customerEntity.getCustomerGender();
    }
}
