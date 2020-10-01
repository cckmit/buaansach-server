package vn.com.buaansach.util.sequence;

import java.time.Instant;

public final class OrderCodeGenerator {
    public static String generate(String storeCode) {
        storeCode = storeCode.replace(CodeConstants.STORE_CODE_PREFIX, "");
        return storeCode + (Instant.now().toEpochMilli() - CodeConstants.CUSTOM_EPOCH) + "";
    }
}
