package vn.com.buaansach.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {
    private static final int DEF_COUNT = 20;

    private static final int DEF_PASSWORD = 12;

    private RandomUtil() {
    }

    /**
     * Generate a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key
     */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Random  a password.
     *
     * @return the generated password
     */
    public static String ranDomPassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_PASSWORD);
    }
}
