package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.UserType;
import vn.com.buaansach.util.sequence.CodeConstants;
import vn.com.buaansach.web.admin.repository.common.AdminProductRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;
import vn.com.buaansach.web.admin.repository.user.AdminUserRepository;

@Service
@RequiredArgsConstructor
public class AdminCodeService {
    private final AdminStoreRepository adminStoreRepository;
    private final AdminProductRepository adminProductRepository;
    private final AdminUserRepository adminUserRepository;

    public String generateCodeForStore() {
        String lastCode = adminStoreRepository.findLastStoreCode();
        int lastNumber = lastCode == null ? 0 : Integer.parseInt(lastCode.substring(CodeConstants.STORE_CODE_PREFIX.length()));
        int newNumber = lastNumber + 1;
        return CodeConstants.STORE_CODE_PREFIX + StringUtils.leftPad("" + newNumber, CodeConstants.STORE_CODE_SUFFIX_LENGTH, "0");
    }

    public String generateCodeForProduct() {
        String lastCode = adminProductRepository.findLastProductCode();
        int lastNumber = lastCode == null ? 0 : Integer.parseInt(lastCode.substring(CodeConstants.PRODUCT_CODE_PREFIX.length()));
        int newNumber = lastNumber + 1;
        return CodeConstants.PRODUCT_CODE_PREFIX + StringUtils.leftPad("" + newNumber, CodeConstants.PRODUCT_CODE_SUFFIX_LENGTH, "0");
    }

    public String generateCodeForUser() {
        String prefix = CodeConstants.INTERNAL_USER_CODE_PREFIX;
        int suffixLength = CodeConstants.INTERNAL_USER_CODE_SUFFIX_LENGTH;
        int nextNumber = adminUserRepository.countByUserType(UserType.INTERNAL) + 1;
        return prefix + StringUtils.leftPad("" + nextNumber, suffixLength, "0");
    }
}
