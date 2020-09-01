package vn.com.buaansach.util;

public final class Constants {
    public static final String DEFAULT_LANGUAGE = "vi";
    public static final String DEFAULT_AREA_COLOR = "#ffffff";
    public static final String LOGIN_REGEX = "^[^_0-9][_0-9a-z]{3,}$"; // Bắt đầu với 1 kí tự bất kì khác _0-9, tiếp theo tối thiểu 3 kí tự thuộc _0-9a-z
//    public static final String LOGIN_REGEX = "^[^0][_'.@A-Za-z0-9-]*$";
    public static final String PHONE_REGEX = "^(09|03|07|08|05)+([0-9]{8})$";
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final String STORE_IMAGE_PATH = "store_images";
    public static final String PRODUCT_IMAGE_PATH = "product_images";
    public static final String PRODUCT_THUMBNAIL_PATH = "product_thumbnails";
    public static final String CATEGORY_IMAGE_PATH = "category_images";
    public static final String USER_IMAGE_PATH = "user_images";
    public static final int POSITION_INCREMENT = (int) Math.pow(2, 16);
    public static final long DEFAULT_FIRST_REG_VOUCHER_ID = 1L;

    private Constants() {
    }
}
