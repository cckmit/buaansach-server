package vn.com.buaansach.util.sequence;

import vn.com.buaansach.web.admin.service.AdminCodeService;

import java.time.Instant;

public final class OrderCodeGenerator {
    /*Sunday, 21 June 2020 00:00:00 UTC+0*/
    private static final long CUSTOM_EPOCH = 1592697600000L;

    public static String generate(String storeCode) {
        storeCode = storeCode.replace(AdminCodeService.STORE_CODE_PREFIX, "");
        return storeCode + (Instant.now().toEpochMilli() - CUSTOM_EPOCH) + "";
    }
}
