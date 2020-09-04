package vn.com.buaansach.util.sequence;

public class CustomerCodeGenerator {
    /*Sunday, 21 June 2020 00:00:00 UTC+0*/
    private static final long CUSTOM_EPOCH = 1592697600000L;

//    public static String generate() {
//        return (Instant.now().toEpochMilli() - CUSTOM_EPOCH) + "";
//    }

    public static String generate(String storeCode, int customerCount) {
        String code = storeCode.replace(CodeConstants.STORE_CODE_PREFIX, CodeConstants.CUSTOMER_CODE_PREFIX);
        return code + "T" + (customerCount + 1);
    }
}
