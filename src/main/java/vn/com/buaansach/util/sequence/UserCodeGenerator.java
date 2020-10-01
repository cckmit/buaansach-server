package vn.com.buaansach.util.sequence;

import java.time.Instant;

public final class UserCodeGenerator {
    public static String generateForCustomer() {
        return CodeConstants.CUSTOMER_CODE_PREFIX + (Instant.now().toEpochMilli() - CodeConstants.CUSTOM_EPOCH);
    }
}
