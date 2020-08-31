package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.entity.enumeration.CustomerZaloStatus;
import vn.com.buaansach.entity.store.StoreEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;
import vn.com.buaansach.web.pos.repository.PosCustomerRepository;
import vn.com.buaansach.web.pos.repository.PosStoreRepository;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosCustomerDTO;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosCustomerService {
    private final PosCustomerRepository posCustomerRepository;
    private final PosStoreRepository posStoreRepository;
    private final PasswordEncoder passwordEncoder;
    private final PosVoucherCodeService posVoucherCodeService;
    private final PosStoreSecurity posStoreSecurity;

    @Transactional
    public PosCustomerDTO posCreateCustomer(PosCustomerDTO payload) {
        StoreEntity storeEntity = posStoreRepository.findOneByGuid(payload.getCustomerStoreGuid())
                .orElseThrow(() -> new NotFoundException("pos@storeNotFound@" + payload.getCustomerStoreGuid()));

        posStoreSecurity.blockAccessIfNotInStore(payload.getCustomerStoreGuid());

        if (posCustomerRepository.findOneByCustomerPhone(payload.getCustomerPhone()).isEmpty()) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setCustomerStoreGuid(payload.getCustomerStoreGuid());
            customerEntity.setCustomerPhone(payload.getCustomerPhone());
            customerEntity.setCustomerGender(payload.getCustomerGender());
            customerEntity.setCustomerName(payload.getCustomerName());
            return new PosCustomerDTO(createCustomer(customerEntity, storeEntity));
        }

        throw new BadRequestException("pos@customerPhoneExist@" + payload.getCustomerPhone());
    }

    public PosCustomerDTO getCustomerByPhone(String customerPhone) {
        return new PosCustomerDTO(posCustomerRepository.findOneByCustomerPhone(customerPhone)
                .orElseThrow(() -> new NotFoundException("pos@customerPhoneNotFound@" + customerPhone)));
    }

    @Transactional
    public CustomerEntity createCustomer(CustomerEntity customerEntity, StoreEntity storeEntity) {
        int customerCount = posCustomerRepository.countByStoreGuid(storeEntity.getGuid());
        String customerCode = "";
        customerEntity.setGuid(UUID.randomUUID());
        customerEntity.setCustomerCode(customerCode);
        customerEntity.setCustomerPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
        customerEntity.setCustomerActivated(true);
        /* mặc định nhân viên tạo cho khách thì khách đã có zalo */
        customerEntity.setCustomerZaloStatus(CustomerZaloStatus.EXIST);
        customerEntity.setCustomerLangKey(Constants.DEFAULT_LANGUAGE);
        CustomerEntity result = posCustomerRepository.save(customerEntity);
        // to make sure save customer successfully then create voucher for customer
        posVoucherCodeService.createVoucherForCustomerRegistration(customerEntity.getCustomerPhone(), customerCode);
        return result;
    }
}
