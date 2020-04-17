package vn.com.buaansach.web.user.service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AuditDTO {
    protected String createdBy;
    protected Instant createdDate;
    protected String lastModifiedBy;
    protected Instant lastModifiedDate;
}
