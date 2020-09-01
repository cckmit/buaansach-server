package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.buaansach.util.sequence.CodeConstants;
import vn.com.buaansach.web.admin.repository.common.AdminProductRepository;
import vn.com.buaansach.web.admin.repository.store.AdminStoreRepository;

@Service
@RequiredArgsConstructor
public class AdminCodeService {
    private final AdminStoreRepository adminStoreRepository;
    private final AdminProductRepository adminProductRepository;

    public String generateCodeForStore() {
        String lastCode = adminStoreRepository.findLastStoreCode();
        long lastNumber = lastCode == null ? 0L : Long.parseLong(lastCode.substring(CodeConstants.STORE_CODE_PREFIX.length()));
        long newNumber = lastNumber + 1L;
        return CodeConstants.STORE_CODE_PREFIX + StringUtils.leftPad("" + newNumber, CodeConstants.STORE_CODE_NUMBER_LENGTH, "0");
    }

    public String generateCodeForProduct() {
        String lastCode = adminProductRepository.findLastProductCode();
        long lastNumber = lastCode == null ? 0L : Long.parseLong(lastCode.substring(CodeConstants.PRODUCT_CODE_PREFIX.length()));
        long newNumber = lastNumber + 1L;
        return CodeConstants.PRODUCT_CODE_PREFIX + StringUtils.leftPad("" + newNumber, CodeConstants.PRODUCT_CODE_NUMBER_LENGTH, "0");
    }
}
