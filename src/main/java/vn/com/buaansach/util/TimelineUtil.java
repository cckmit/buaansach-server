package vn.com.buaansach.util;

import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.OrderTimelineStatus;

import java.time.Instant;

public final class TimelineUtil {
    private static final int MAX_ORDER_STATUS_TIMELINE = 3000;
    private static final int MAX_ORDER_PRODUCT_STATUS_TIMELINE = 1000;

    public static String initOrderStatus(OrderTimelineStatus status, String actor) {
        return status.name() + "@" + actor + "@" + Instant.now().toString();
    }

    public static String appendOrderStatus(String currentTimeline, OrderTimelineStatus newStatus, String actor) {
        String newTimeline = currentTimeline + ";" + newStatus.name() + "@" + actor + "@" + Instant.now().toString();
        return newTimeline.length() > MAX_ORDER_STATUS_TIMELINE ? currentTimeline : newTimeline;
    }

    public static String appendOrderStatusWithMeta(String currentTimeline, OrderTimelineStatus newStatus, String actor, String metaData) {
        /* meta data separated by '*' */
        String newTimeline = currentTimeline + ";" + newStatus + "@" + actor + "@" + Instant.now().toString() + "@" + metaData;
        return newTimeline.length() > MAX_ORDER_STATUS_TIMELINE ? currentTimeline : newTimeline;
    }

    public static String initOrderProductStatus(OrderProductStatus status, String actor) {
        return status.name() + "@" + actor + "@" + Instant.now().toString();
    }

    public static String appendOrderProductStatus(String currentTimeline, OrderProductStatus newStatus, String actor) {
        String newTimeline = currentTimeline + ";" + newStatus.name() + "@" + actor + "@" + Instant.now().toString();
        return newTimeline.length() > MAX_ORDER_PRODUCT_STATUS_TIMELINE ? currentTimeline : newTimeline;
    }
}
