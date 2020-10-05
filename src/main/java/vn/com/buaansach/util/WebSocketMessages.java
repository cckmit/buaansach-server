package vn.com.buaansach.util;

public final class WebSocketMessages {
    /*Send to Guest*/
    public static final String POS_RECEIVE_ORDER = "POS_RECEIVE_ORDER";
    public static final String POS_CANCEL_ORDER = "POS_CANCEL_ORDER";
    public static final String POS_PURCHASE_ORDER = "POS_PURCHASE_ORDER";
    public static final String POS_CHANGE_SEAT = "POS_CHANGE_SEAT";
    public static final String POS_LOCK_SEAT = "POS_LOCK_SEAT";
    public static final String POS_UPDATE_STORE_STATUS = "POS_UPDATE_STORE_STATUS";
    public static final String POS_UPDATE_STORE_PRODUCT = "POS_UPDATE_STORE_PRODUCT";

    /*Send to POS*/
    public static final String POS_UPDATE_ORDER = "POS_UPDATE_ORDER";
    public static final String GUEST_CREATE_ORDER = "GUEST_CREATE_ORDER";
    public static final String GUEST_UPDATE_ORDER = "GUEST_UPDATE_ORDER";
    public static final String GUEST_CALL_WAITER = "GUEST_CALL_WAITER";
    public static final String GUEST_STORE_PAY_REQUEST = "GUEST_STORE_PAY_REQUEST";
    public static final String GUEST_UPDATE_POINT = "GUEST_UPDATE_POINT";
    public static final String GUEST_UPDATE_PHONE = "GUEST_UPDATE_PHONE";


}
