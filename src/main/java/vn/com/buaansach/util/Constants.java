package vn.com.buaansach.util;

public final class Constants {
    public static final String DEFAULT_LANGUAGE = "vi";
    public static final String LOGIN_REGEX = "^[^_0-9][_0-9A-Za-z]{3,}$"; // Bắt đầu với 1 kí tự bất kì khác _0-9, tiếp theo tối thiểu 3 kí tự thuộc _0-9A-Za-z
    public static final String PHONE_REGEX = "^(09|03|07|08|05)+([0-9]{8})$";
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String STORE_IMAGE_PATH = "store_images";
    public static final String PRODUCT_IMAGE_PATH = "product_images";
    public static final String SALE_IMAGE_PATH = "sale_images";
    public static final String CATEGORY_IMAGE_PATH = "category_images";
    public static final String USER_IMAGE_PATH = "user_images";
    public static final String BANNER_IMAGE_PATH = "banner_images";
    public static final int POSITION_INCREMENT = (int) Math.pow(2, 16);
    public static final int VND_PER_POINT = 50;

    private Constants() {
    }
}
