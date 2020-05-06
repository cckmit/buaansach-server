package vn.com.buaansach.web.admin.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.buaansach.web.admin.repository.AdminProductRepository;
import vn.com.buaansach.web.admin.repository.AdminStoreRepository;
import vn.com.buaansach.web.admin.repository.AdminUserRepository;

@Service
public class AdminCodeService {
    private static final String STORE_CODE_PREFIX = "BAS";
    private static final String USER_CODE_PREFIX = "NV";
    private static final String PRODUCT_CODE_PREFIX = "SP";
    private static final int LONG_SUFFIX = 5;
    private static final int MEDIUM_SUFFIX = 4;
    private static final int SHORT_SUFFIX = 3;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminUserRepository adminUserRepository;
    private final AdminProductRepository adminProductRepository;

    public AdminCodeService(AdminStoreRepository adminStoreRepository, AdminUserRepository adminUserRepository, AdminProductRepository adminProductRepository) {
        this.adminStoreRepository = adminStoreRepository;
        this.adminUserRepository = adminUserRepository;
        this.adminProductRepository = adminProductRepository;
    }

    public String generateCodeForUser() {
        String lastUserCode = adminUserRepository.findLastUserCode();
        long lastNumber = lastUserCode == null ? 0L : Long.parseLong(lastUserCode.substring(USER_CODE_PREFIX.length()));
        long newNumber = lastNumber + 1L;
        return USER_CODE_PREFIX + StringUtils.leftPad("" + newNumber, LONG_SUFFIX, "0");
    }

    public String generateCodeForStore() {
        Long lastStoreId = adminStoreRepository.findLastStoreId();
        long newNumber = lastStoreId == null ? 1L : lastStoreId + 1L;
        return STORE_CODE_PREFIX + StringUtils.leftPad("" + newNumber, SHORT_SUFFIX, "0");
    }

    public String generateCodeForProduct() {
        Long lastProductId = adminProductRepository.findLastProductId();
        long newNumber = lastProductId == null ? 1L : lastProductId + 1L;
        return PRODUCT_CODE_PREFIX + StringUtils.leftPad("" + newNumber, MEDIUM_SUFFIX, "0");
    }
}
