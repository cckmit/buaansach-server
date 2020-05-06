package vn.com.buaansach.web.pos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.buaansach.web.pos.repository.PosCustomerRepository;

@Service
public class PosCodeService {
    private static final String CUSTOMER_CODE_PREFIX = "KH";
    private static final int LONG_SUFFIX = 8;
    private final PosCustomerRepository posCustomerRepository;

    public PosCodeService(PosCustomerRepository posCustomerRepository) {
        this.posCustomerRepository = posCustomerRepository;
    }

    public String generateCodeForCustomer() {
        String lastCode = posCustomerRepository.findLastCustomerCode();
        long lastNumber = lastCode == null ? 0L : Long.parseLong(lastCode.substring(CUSTOMER_CODE_PREFIX.length()));
        long newNumber = lastNumber + 1L;
        return CUSTOMER_CODE_PREFIX + StringUtils.leftPad("" + newNumber, LONG_SUFFIX, "0");
    }

}
