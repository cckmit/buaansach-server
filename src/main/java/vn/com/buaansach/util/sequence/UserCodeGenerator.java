package vn.com.buaansach.util.sequence;

import java.time.Instant;

public final class UserCodeGenerator {
    /*2020-01-01 00:00:00 UTC+0*/
    private static final long CUSTOM_EPOCH = 1577836800000L;

    public static String generate() {
        return (Instant.now().toEpochMilli() - CUSTOM_EPOCH) + "";
    }
}
