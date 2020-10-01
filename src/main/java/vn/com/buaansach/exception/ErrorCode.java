package vn.com.buaansach.exception;

public enum ErrorCode {
    /* General */
    APP_ERROR,
    BAD_REQUEST,
    FORBIDDEN,
    NOT_FOUND,
    UNAUTHORIZED,

    /* Specific */
    AREA_NOT_FOUND,
    EMAIL_EXIST,
    INVALID_OPERATION,
    LOGIN_EXIST,
    ORDER_NOT_FOUND,
    PHONE_EXIST,
    SEAT_NOT_FOUND,
    STORE_NOT_FOUND,
    STORE_USER_EXIST,
    USER_NOT_FOUND,
    USER_NOT_ACTIVATED,
    STORE_USER_NOT_FOUND,
    RESET_KEY_NOT_FOUND_OR_EXPIRED,
    CATEGORY_NAME_EXIST,
    CATEGORY_NOT_FOUND,
    PRODUCT_CODE_EXIST,
    PRODUCT_NOT_FOUND,
    CATEGORY_NAME_ENG_EXIST,
    STORE_PRODUCT_NOT_FOUND,
    VOUCHER_NOT_FOUND,
    VOUCHER_CODE_NOT_FOUND,
    INCORRECT_CURRENT_PASSWORD,
    AREA_DISABLED,
    SEAT_LOCKED,
    SEAT_NON_EMPTY,
    STORE_PAY_REQUEST_EXIST,
    PAY_AMOUNT_NOT_ENOUGH,
    ORDER_AND_SEAT_NOT_MATCH,
    LIST_ORDER_PRODUCT_EMPTY,
    ORDER_PURCHASED,
    ORDER_CANCELLED,
    STORE_PRODUCT_STOP_TRADING,
    STORE_PRODUCT_UNAVAILABLE,
    USER_NOT_IN_STORE,
    STORE_CLOSED_OR_DEACTIVATED,
    VOUCHER_DISABLED,
    VOUCHER_CODE_DISABLED,
    VOUCHER_CODE_MAX_USED,
    VOUCHER_EXPIRED,
    VOUCHER_UNUSABLE,
    VOUCHER_CODE_AND_PHONE_NOT_MATCH,
    VOUCHER_CODE_INVALID,
    PRODUCT_STOP_TRADING,
    ORDER_PRODUCT_NOT_FOUND,
    ORDER_PRODUCT_NOT_MATCH_ORDER,
    ORDER_PRODUCT_CANCEL_REASON_REQUIRED,
    INVALID_ORDER_STATUS,
    SEAT_NOT_IN_SAME_STORE,
    NOTIFICATION_NOT_IN_STORE,
    STORE_NOTIFICATION_NOT_FOUND,
    USER_PROFILE_NOT_FOUND,
    SALE_NOT_FOUND,
    INVALID_SALE_TIME_CONDITION,
    SALE_DISABLED,
    SALE_NOT_STARTED,
    SALE_ENDED,
    STORE_SALE_NOT_FOUND,
    STORE_SALE_DISABLED,
    FILE_NOT_FOUND,
    TOPIC_SUBSCRIPTION_DENIED,
    BANNER_IMAGE_CANNOT_BE_NULL,
    BANNER_NOT_FOUND,
    PRODUCT_NOT_ACTIVATED,
    ORDER_AND_SEAT_SIZE_NOT_EQUAL,
    LIST_PURCHASE_HAS_EMPTY_SEAT,
    LIST_PURCHASE_HAS_UNFINISHED_SEAT,
    SOME_ORDER_NOT_FOUND,
    LIST_SEAT_GUID_EMPTY,
    VERSION_NOT_FOUND,
    ORDER_CUSTOMER_PHONE_EMPTY,
    CUSTOMER_NOT_FOUND,
    CUSTOMER_POINT_NOT_ENOUGH,
    POINT_USAGE_MUST_GREATER_THAN_EQUAL_ZERO,
    USER_PHONE_NOT_MATCH_ORDER_PHONE,
    ORDER_POINT_VALUE_NOT_MATCH,
    LIST_SEAT_SIZE_NOT_MATCH,
}
