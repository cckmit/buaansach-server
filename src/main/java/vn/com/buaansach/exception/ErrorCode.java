package vn.com.buaansach.exception;

public enum ErrorCode {
    APP_ERROR, // Lỗi máy chủ
    BAD_REQUEST, // Yêu cầu không hợp lệ
    FORBIDDEN, // Yêu cầu bị chặn
    NOT_FOUND, // Không tìm thấy dữ liệu
    UNAUTHORIZED, // Yêu cầu Chưa được xác thực
    AREA_NOT_FOUND, // Không tìm thấy khu vực
    EMAIL_EXIST, // Email đã tồn tại
    INVALID_OPERATION, // Thao tác không hợp lệ
    LOGIN_EXIST, // Tên đăng nhập đã tồn tại
    ORDER_NOT_FOUND, // Không tìm thấy đơn hàng
    PHONE_EXIST, // Số điện thoại đã tồn tại
    SEAT_NOT_FOUND, // Không tìm thấy chỗ ngồi
    STORE_NOT_FOUND, // Không tìm thấy cửa hàng
    STORE_USER_EXIST, // Nhân viên đã tồn tại trong cửa hàng
    USER_NOT_FOUND, // Không tìm thấy người dùng
    USER_NOT_ACTIVATED, // Người dùng chưa được kích hoạt
    STORE_USER_NOT_FOUND, // Không tìm thấy nhân viên
    RESET_KEY_NOT_FOUND_OR_EXPIRED, // Mã khôi phục đã hết hạn hoặc không tìm thấy
    CATEGORY_NAME_EXIST, // Tên danh mục đã tồn tại
    CATEGORY_NOT_FOUND, // Không tìm thấy danh mục
    PRODUCT_CODE_EXIST, // Mã sản phẩm đã tồn tại
    PRODUCT_NOT_FOUND, // Không tìm thấy sản phẩm
    CATEGORY_NAME_ENG_EXIST, // Tên danh mục tiếng anh đã tồn tại
    STORE_PRODUCT_NOT_FOUND, // Không tìm thấy sản phẩm của cửa hàng
    VOUCHER_NOT_FOUND, // Không tìm thấy voucher
    VOUCHER_CODE_NOT_FOUND, // Không tìm thấy mã voucher
    INCORRECT_CURRENT_PASSWORD, // Mật khẩu hiện tại không đúng
    AREA_DISABLED, // Khu vực đã bị khóa
    SEAT_LOCKED, // Chỗ ngồi đã bị khóa
    SEAT_NON_EMPTY, // Chỗ ngồi đã được sử dụng
    STORE_PAY_REQUEST_EXIST, // Yêu cầu thanh toán đã tồn tại
    PAY_AMOUNT_NOT_ENOUGH, // Tiền thanh toán không đủ
    ORDER_AND_SEAT_NOT_MATCH, // Mã đơn hàng không khớp vị trí hiện tại
    LIST_ORDER_PRODUCT_EMPTY, // Danh sách gọi sản phẩm trống
    ORDER_PURCHASED, // Đơn đã được thanh toán
    ORDER_CANCELLED, // Đơn đã bị hủy
    STORE_PRODUCT_STOP_TRADING, // Sản phẩm đã ngừng kinh doanh
    STORE_PRODUCT_UNAVAILABLE, // Sản phẩm đã hết hàng
    USER_NOT_IN_STORE, // Người dùng không thuộc cửa hàng
    STORE_CLOSED_OR_DEACTIVATED, // Cửa hàng đã đóng cửa hoặc bị vô hiệu hóa
    VOUCHER_DISABLED, // Voucher đã bị vô hiệu hóa
    VOUCHER_CODE_DISABLED, // Mã voucher đã bị vô hiệu hóa
    VOUCHER_CODE_MAX_USED, // Mã voucher đã hết lượt sử dụng
    VOUCHER_EXPIRED, // Voucher đã hết hạn
    VOUCHER_UNUSABLE, // Voucher không khả dụng
    VOUCHER_CODE_AND_PHONE_NOT_MATCH, // Mã voucher không đúng với số điện thoại
    VOUCHER_CODE_INVALID, // Mã voucher không hợp lệ
    PRODUCT_STOP_TRADING, // Sản phẩm đã ngừng kinh doanh
    ORDER_PRODUCT_NOT_FOUND, // Không tìm thấy sản phẩm đã gọi
    ORDER_PRODUCT_NOT_MATCH_ORDER, // Sản phẩm đã gọi không khớp đơn hàng
    ORDER_PRODUCT_CANCEL_REASON_REQUIRED, // Lí do hủy sản phẩm không được bỏ trống
    INVALID_ORDER_STATUS, // Trạng thái đơn hàng không hợp lệ
    SEAT_NOT_IN_SAME_STORE, // Chỗ ngồi không trong cùng cửa hàng
    NOTIFICATION_NOT_IN_STORE, // Thông báo không thuộc cửa hàng này
    STORE_NOTIFICATION_NOT_FOUND, // Không tìm thấy thông báo của cửa hàng
    USER_PROFILE_NOT_FOUND, // Không tìm thấy thông tin người dùng
    SALE_NOT_FOUND, // Không tìm thấy khuyến mãi
    INVALID_SALE_TIME_CONDITION, // Thời gian khuyến mãi không hợp lệ
    SALE_DISABLED, // Khuyến mãi đã bị vô hiệu hóa
    SALE_NOT_STARTED, // Chưa tới thời gian bắt đầu của khuyến mãi
    SALE_ENDED, // Khuyến mãi đã kết thúc
    STORE_SALE_NOT_FOUND, // Không tìm thấy khuyến mãi của cửa hàng
    STORE_SALE_DISABLED, // Khuyến mãi của cửa hàng đã bị vô hiệu hóa
    FILE_NOT_FOUND, // Không tìm thấy file
    TOPIC_SUBSCRIPTION_DENIED, // Không thể đăng ký vào topic
    BANNER_IMAGE_CANNOT_BE_NULL, // Ảnh banner bắt buộc phải có
    BANNER_NOT_FOUND, // Không tìm thấy banner
    PRODUCT_NOT_ACTIVATED, // Sản phẩm chưa được kích hoạt
    ORDER_AND_SEAT_SIZE_NOT_EQUAL, // Số đơn và số chỗ không bằng nhau
    LIST_PURCHASE_HAS_EMPTY_SEAT, // Danh sách thanh toán có vị trí bị trống
    LIST_PURCHASE_HAS_UNFINISHED_SEAT, // Danh sách thanh toán có vị trí chưa phục vụ xong
    SOME_SEAT_NOT_FOUND, // Có vị trí không thể tìm thấy
    LIST_SEAT_GUID_EMPTY, // Danh vị trí trống
    VERSION_NOT_FOUND, // Không tìm thấy phiên bản
    ORDER_CUSTOMER_PHONE_EMPTY, // Đơn hàng chưa có số điện thoại
    CUSTOMER_NOT_FOUND, // Không tìm thấy khách hàng
    CUSTOMER_POINT_NOT_ENOUGH, // Không đủ điểm thưởng
    POINT_USAGE_MUST_GREATER_THAN_EQUAL_ZERO, // Điểm thưởng sử dụng phải lớn hơn hoặc bằng 0
    USER_PHONE_NOT_MATCH_ORDER_PHONE, // Số điện thoại tài khoản đăng nhập khác số điện thoại trên đơn
    ORDER_INFO_OUT_OF_DATE, // Thông tin đơn hàng đã thay đổi bấm làm mới và thử lại
    STORE_WORK_SHIFT_NOT_FOUND, // Không tìm thấy ca làm việc
    ORDER_UNFINISHED, // Đơn chưa được phục vụ xong
    AREA_RESTRICTED, // Khu vực bạn vừa thao tác bị giới hạn
}
