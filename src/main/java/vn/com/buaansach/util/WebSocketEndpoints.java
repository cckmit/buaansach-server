package vn.com.buaansach.util;

public final class WebSocketEndpoints {
    /* All send */
    public static final String APP_ACTIVITY = "/activity";

    /* === SUBSCRIPTION === */
    /* Admin only */
    public static final String TOPIC_ADMIN_TRACKER = "/topic/admin/tracker";
    /* POS */
    public static final String TOPIC_POS_PREFIX = "/topic/pos/";
    /* Guest */
    public static final String TOPIC_GUEST_PREFIX = "/topic/guest/";
}
