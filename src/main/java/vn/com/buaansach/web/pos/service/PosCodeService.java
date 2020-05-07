package vn.com.buaansach.web.pos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.customer.CustomerEntity;
import vn.com.buaansach.web.pos.repository.PosCustomerRepository;

import javax.transaction.Transactional;

@Service
public class PosCodeService {
    private static final String CUSTOMER_CODE_PREFIX = "KH";
    private static final int LONG_SUFFIX = 8;
    private final PosCustomerRepository posCustomerRepository;

    public PosCodeService(PosCustomerRepository posCustomerRepository) {
        this.posCustomerRepository = posCustomerRepository;
    }

    public String generateCodeForCustomer() {
        PageRequest request = PageRequest.of(0, 1, Sort.Direction.DESC, "id");
        Page<CustomerEntity> page = posCustomerRepository.findPageCustomer(request);
        String lastCode = page.getSize() == 0 ? null : page.getContent().get(0).getCustomerCode();
        long lastNumber = lastCode == null ? 0L : Long.parseLong(lastCode.substring(CUSTOMER_CODE_PREFIX.length()));
        long newNumber = lastNumber + 1L;
        return CUSTOMER_CODE_PREFIX + StringUtils.leftPad("" + newNumber, LONG_SUFFIX, "0");
    }

}
