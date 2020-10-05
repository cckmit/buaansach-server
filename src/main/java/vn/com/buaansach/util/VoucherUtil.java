package vn.com.buaansach.util;

import org.apache.commons.lang3.RandomStringUtils;

public final class VoucherUtil {
    public static int VOUCHER_CODE_LENGTH = 6;
    public static String generateVoucherCode(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
