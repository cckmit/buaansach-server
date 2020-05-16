package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;

@Data
public class PosSaleReportDTO {
    private long totalRevenue;
    private long totalOrderCount;
    private long totalPurchasedCount;
    private long totalCancelledCount;
}
