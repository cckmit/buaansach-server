package vn.com.buaansach.util.sequence;

import vn.com.buaansach.web.admin.service.AdminCodeService;

import java.time.Instant;

public final class CustomerCodeGenerator {
    /*Sunday, 21 June 2020 00:00:00 UTC+0*/
    private static final long CUSTOM_EPOCH = 1592697600000L;
    public static final String CUSTOMER_CODE_PREFIX = "KH";

//    public static String generate() {
//        return (Instant.now().toEpochMilli() - CUSTOM_EPOCH) + "";
//    }

    public static String generate(String storeCode, int customerCount) {
        String code = storeCode.replace(AdminCodeService.STORE_CODE_PREFIX, CUSTOMER_CODE_PREFIX);
        return code + "S" + (customerCount + 1);
    }
}
