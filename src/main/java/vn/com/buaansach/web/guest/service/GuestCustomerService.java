package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;
import vn.com.buaansach.web.guest.repository.GuestCustomerRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestCustomerService {
    private final GuestCustomerRepository guestCustomerRepository;
    private final GuestCodeService guestCodeService;
    private final PasswordEncoder passwordEncoder;
    private final GuestVoucherCodeService guestVoucherCodeService;

    @Transactional
    public void guestCreateCustomerIfNotExist(String customerName, String customerPhone) {
        if (guestCustomerRepository.findOneByCustomerPhone(customerPhone).isEmpty()) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setCustomerPhone(customerPhone);
            customerEntity.setCustomerName(customerName);
            createCustomer(customerEntity);
        }
    }

    @Transactional
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
        customerEntity.setGuid(UUID.randomUUID());
        customerEntity.setCustomerCode(guestCodeService.generateCodeForCustomer(customerEntity.getCustomerPhone(), customerEntity.getCreatedDate()));
        customerEntity.setCustomerPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
        customerEntity.setCustomerActivated(true);
        customerEntity.setCustomerLangKey(Constants.DEFAULT_LANGUAGE);
        CustomerEntity result = guestCustomerRepository.save(customerEntity);
        // save customer first to make sure save customer successfully then create voucher for customer
        guestVoucherCodeService.createVoucherForCustomerRegistration(customerEntity.getCustomerPhone());
        return result;
    }
}
