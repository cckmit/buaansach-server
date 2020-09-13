package vn.com.buaansach.web.admin.service.dto.readwrite;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class AdminPageViewDTO {
    @NotNull
    private Instant startDate;
    private Instant endDate;
    private int totalPageView;
    private int uniqueVisitor;
}
