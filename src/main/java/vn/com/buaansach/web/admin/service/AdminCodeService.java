package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.buaansach.web.admin.repository.AdminProductRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;

@Service
@RequiredArgsConstructor
public class AdminCodeService {
    private static final String STORE_CODE_PREFIX = "BAS";
    private static final String PRODUCT_CODE_PREFIX = "SP";
    private static final int STORE_CODE_NUMBER_LENGTH = 3;
    private static final int PRODUCT_CODE_NUMBER_LENGTH = 4;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminProductRepository adminProductRepository;

    public String generateCodeForStore() {
        String lastCode = adminStoreRepository.findLastStoreCode();
        long lastNumber = lastCode == null ? 0L : Long.parseLong(lastCode.substring(STORE_CODE_PREFIX.length()));
        long newNumber = lastNumber + 1L;
        return STORE_CODE_PREFIX + StringUtils.leftPad("" + newNumber, STORE_CODE_NUMBER_LENGTH, "0");
    }

    public String generateCodeForProduct() {
        String lastCode = adminProductRepository.findLastProductCode();
        long lastNumber = lastCode == null ? 0L : Long.parseLong(lastCode.substring(PRODUCT_CODE_PREFIX.length()));
        long newNumber = lastNumber + 1L;
        return PRODUCT_CODE_PREFIX + StringUtils.leftPad("" + newNumber, PRODUCT_CODE_NUMBER_LENGTH, "0");
    }
}
