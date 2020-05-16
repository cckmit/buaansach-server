package vn.com.buaansach.web.pos.service.dto.read;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.UUID;

@Data
public class PosSaleReportParams {
    private UUID storeGuid;
    private String userLogin;
    private Instant startDate;
    private Instant endDate;
    private String orderCode;
    private int page;
    private int size;
    private String sortField;
    private Sort.Direction sortDirection;
}
