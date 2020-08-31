package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.web.guest.repository.GuestCustomerRepository;

@Service
@RequiredArgsConstructor
public class GuestCustomerService {
    private final GuestCustomerRepository guestCustomerRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuestVoucherCodeService guestVoucherCodeService;

//    @Transactional
//    public void guestCreateCustomerIfNotExist(String customerName, String customerPhone) {
//        if (guestCustomerRepository.findOneByCustomerPhone(customerPhone).isEmpty()) {
//            CustomerEntity customerEntity = new CustomerEntity();
//            customerEntity.setCustomerPhone(customerPhone);
//            customerEntity.setCustomerName(customerName);
//            createCustomer(customerEntity);
//        }
//    }
//
//    @Transactional
//    public GuestCustomerDTO guestCreateCustomer(GuestCustomerDTO payload) {
//        if (guestCustomerRepository.findOneByCustomerPhone(payload.getCustomerPhone()).isEmpty()) {
//            CustomerEntity customerEntity = new CustomerEntity();
//            customerEntity.setCustomerPhone(payload.getCustomerPhone());
//            customerEntity.setCustomerGender(payload.getCustomerGender());
//            customerEntity.setCustomerName(payload.getCustomerName());
//            return new GuestCustomerDTO(createCustomer(customerEntity));
//        }
//        throw new BadRequestException("guest@customerPhoneExist@" + payload.getCustomerPhone());
//    }
//
//    @Transactional
//    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
//        customerEntity.setGuid(UUID.randomUUID());
//        customerEntity.setCustomerCode(CustomerCodeGenerator.generate());
//        customerEntity.setCustomerPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
//        customerEntity.setCustomerActivated(true);
//        customerEntity.setCustomerZaloStatus(CustomerZaloStatus.UNKNOWN);
//        customerEntity.setCustomerLangKey(Constants.DEFAULT_LANGUAGE);
//        CustomerEntity result = guestCustomerRepository.save(customerEntity);
//        // save customer first to make sure save customer successfully then create voucher for customer
//        guestVoucherCodeService.createVoucherForCustomerRegistration(customerEntity.getCustomerPhone());
//        return result;
//    }
}
