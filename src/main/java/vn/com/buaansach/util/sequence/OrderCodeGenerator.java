package vn.com.buaansach.util.sequence;

import java.time.Instant;

public final class OrderCodeGenerator {
    /*1970-01-01 00:00:00 UTC+0*/
    private static final long CUSTOM_EPOCH = 0L;

    public static String generate() {
        return (Instant.now().toEpochMilli() - CUSTOM_EPOCH) + "";
    }
}
