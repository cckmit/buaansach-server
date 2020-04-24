package vn.com.buaansach.web.pos.util;

import vn.com.buaansach.entity.enumeration.OrderProductStatus;
import vn.com.buaansach.entity.enumeration.OrderStatus;

import java.time.Instant;

public final class TimelineUtil {
    public static String initOrderStatus(OrderStatus status) {
        return status.name() + "@@" + Instant.now().toString();
    }

    public static String initOrderStatus(OrderStatus status, String actor) {
        return status.name() + "@" + actor + "@" + Instant.now().toString();
    }

    public static String appendOrderStatus(String currentTimeline, OrderStatus newStatus) {
        return currentTimeline + ";" + newStatus.name() + "@@" + Instant.now().toString();
    }

    public static String appendOrderStatus(String currentTimeline, OrderStatus newStatus, String actor) {
        return currentTimeline + ";" + newStatus.name() + "@" + actor + "@" + Instant.now().toString();
    }

    public static String initOrderProductStatus(OrderProductStatus status) {
        return status.name() + "@@" + Instant.now().toString();
    }

    public static String initOrderProductStatus(OrderProductStatus status, String actor) {
        return status.name() + "@" + actor + "@" + Instant.now().toString();
    }

    public static String appendOrderProductStatus(String currentTimeline, OrderProductStatus newStatus) {
        return currentTimeline + ";" + newStatus.name() + "@@" + Instant.now().toString();
    }

    public static String appendOrderProductStatus(String currentTimeline, OrderProductStatus newStatus, String actor) {
        return currentTimeline + ";" + newStatus.name() + "@" + actor + "@" + Instant.now().toString();
    }
}
