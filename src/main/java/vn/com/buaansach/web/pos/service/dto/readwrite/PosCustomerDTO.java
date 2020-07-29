package vn.com.buaansach.web.pos.service.dto.readwrite;

import lombok.Data;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.util.Constants;

import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
public class PosCustomerDTO {
    private UUID storeGuid;

    private String customerCode;

    private String customerName;

    private Gender customerGender;

    @Pattern(regexp = Constants.PHONE_REGEX)
    private String customerPhone;

    private int customerPoint;

    public PosCustomerDTO() {
    }

    public PosCustomerDTO(CustomerEntity customerEntity) {
        this.storeGuid = customerEntity.getStoreGuid();
        this.customerCode = customerEntity.getCustomerCode();
        this.customerPhone = customerEntity.getCustomerPhone();
        this.customerName = customerEntity.getCustomerName();
        this.customerGender = customerEntity.getCustomerGender();
        this.customerPoint = customerEntity.getCustomerPoint();
    }
}
