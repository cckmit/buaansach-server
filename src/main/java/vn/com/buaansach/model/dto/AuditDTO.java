package vn.com.buaansach.model.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class AuditDTO {
    protected UUID guid;
    protected String createdBy;
    protected Instant createdDate;
    protected String lastModifiedBy;
    protected Instant lastModifiedDate;
}
