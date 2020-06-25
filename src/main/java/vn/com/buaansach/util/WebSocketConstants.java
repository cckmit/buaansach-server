package vn.com.buaansach.util;

public final class WebSocketConstants {
    public static final String APP_ACTIVITY = "/activity";
    public static final String TOPIC_ADMIN_TRACKER = "/topic/admin/tracker";
    public static final String TOPIC_CUSTOMER_CARE_TRACKER = "/topic/customer_care/tracker";
    public static final String TOPIC_POS_PREFIX = "/topic/pos/";
    public static final String TOPIC_GUEST_PREFIX = "/topic/guest/";

    public static final String POS_CREATE_CUSTOMER = "POS_CREATE_CUSTOMER";
    public static final String POS_RECEIVE_ORDER = "POS_RECEIVE_ORDER";
    public static final String POS_CANCEL_ORDER = "POS_CANCEL_ORDER";
    public static final String POS_PURCHASE_ORDER = "POS_PURCHASE_ORDER";
    public static final String POS_CHANGE_SEAT = "POS_CHANGE_SEAT";

    public static final String GUEST_CREATE_CUSTOMER = "GUEST_CREATE_CUSTOMER";
    public static final String GUEST_UPDATE_ORDER = "GUEST_UPDATE_ORDER";
    public static final String GUEST_CREATE_ORDER = "GUEST_CREATE_ORDER";
}
