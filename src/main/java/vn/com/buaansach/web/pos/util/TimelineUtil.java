package vn.com.buaansach.web.pos.util;

import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.OrderStatus;

import java.time.Instant;

public final class TimelineUtil {

    public static String initOrderStatus(OrderStatus status, String actor) {
        return status.name() + "@" + actor + "@" + Instant.now().toString();
    }

    public static String appendOrderStatus(String currentTimeline, OrderStatus newStatus, String actor) {
        String newTimeline = currentTimeline + ";" + newStatus.name() + "@" + actor + "@" + Instant.now().toString();
        return newTimeline.length() > 1000 ? currentTimeline : newTimeline;
    }

    public static String appendCustomOrderStatus(String currentTimeline, String newStatus, String actor) {
        String newTimeline = currentTimeline + ";" + newStatus + "@" + actor + "@" + Instant.now().toString();
        return newTimeline.length() > 1000 ? currentTimeline : newTimeline;
    }

    public static String initOrderProductStatus(OrderProductStatus status, String actor) {
        return status.name() + "@" + actor + "@" + Instant.now().toString();
    }

    public static String appendOrderProductStatus(String currentTimeline, OrderProductStatus newStatus, String actor) {
        String newTimeline = currentTimeline + ";" + newStatus.name() + "@" + actor + "@" + Instant.now().toString();
        return newTimeline.length() > 1000 ? currentTimeline : newTimeline;
    }
}
