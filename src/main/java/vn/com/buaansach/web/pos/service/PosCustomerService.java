package vn.com.buaansach.web.pos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;
import vn.com.buaansach.web.pos.repository.PosCustomerRepository;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosCustomerDTO;

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
    public void createCustomerByPhone(String customerPhone) {
        if (posCustomerRepository.findOneByCustomerPhone(customerPhone).isEmpty()) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setCustomerPhone(customerPhone);
            createCustomer(customerEntity);
        }
    }

    @Transactional
    public PosCustomerDTO posCreateCustomer(PosCustomerDTO payload) {
        if (posCustomerRepository.findOneByCustomerPhone(payload.getCustomerPhone()).isEmpty()) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setCustomerPhone(payload.getCustomerPhone());
            customerEntity.setCustomerName(payload.getCustomerName());
            return new PosCustomerDTO(createCustomer(customerEntity));
        }
        throw new BadRequestException("Customer phone already exists" + payload.getCustomerPhone());
    }

    public PosCustomerDTO getCustomerByPhone(String customerPhone) {
        return new PosCustomerDTO(posCustomerRepository.findOneByCustomerPhone(customerPhone)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with phone: " + customerPhone)));
    }

    @Transactional
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {

        PageRequest request = PageRequest.of(0, 1, Sort.Direction.DESC, "id");
        Page<CustomerEntity> page = posCustomerRepository.findPageCustomer(request);
        String lastCode = page.getContent().size() == 0 ? null : page.getContent().get(0).getCustomerCode();
        customerEntity.setGuid(UUID.randomUUID());
        customerEntity.setCustomerCode(posCodeService.generateCodeForCustomer(lastCode));
        customerEntity.setCustomerPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
        customerEntity.setCustomerActivated(true);
        customerEntity.setCustomerLangKey(Constants.DEFAULT_LANGUAGE);
        CustomerEntity result = posCustomerRepository.save(customerEntity);
        // to make sure save customer successfully then create voucher for customer
        posVoucherCodeService.createVoucherForCustomerRegistration(customerEntity.getCustomerPhone());
        return result;
    }
}
