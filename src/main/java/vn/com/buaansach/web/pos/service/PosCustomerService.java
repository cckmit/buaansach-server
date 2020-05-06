package vn.com.buaansach.web.pos.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.util.RandomUtil;
import vn.com.buaansach.web.pos.repository.PosCustomerRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class PosCustomerService {
    private final PosCustomerRepository posCustomerRepository;
    private final PosCodeService posCodeService;
    private final PasswordEncoder passwordEncoder;
    private final PosVoucherCodeService posVoucherCodeService;

    public PosCustomerService(PosCustomerRepository posCustomerRepository, PosCodeService posCodeService, PasswordEncoder passwordEncoder, PosVoucherCodeService posVoucherCodeService) {
        this.posCustomerRepository = posCustomerRepository;
        this.posCodeService = posCodeService;
        this.passwordEncoder = passwordEncoder;
        this.posVoucherCodeService = posVoucherCodeService;
    }

    @Transactional
    public void createCustomerIfNotExist(String customerPhone) {
        if (posCustomerRepository.findOneByCustomerPhone(customerPhone).isEmpty()) {
            createCustomer(customerPhone);
            posVoucherCodeService.createVoucherForCustomerRegistration(customerPhone);
        }
    }

    @Transactional
    public void createCustomer(String customerPhone) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setGuid(UUID.randomUUID());
        customerEntity.setCustomerCode(posCodeService.generateCodeForCustomer());
        customerEntity.setCustomerPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
        customerEntity.setCustomerActivated(true);
        customerEntity.setCustomerPhone(customerPhone);
        posCustomerRepository.save(customerEntity);
    }
}
