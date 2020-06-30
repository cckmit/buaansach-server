package vn.com.buaansach.web.guest.websocket.dto;

import lombok.Data;
import vn.com.buaansach.web.guest.websocket.enumeration.ActiveStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Data
public class ActivityDTO {
    private ActiveStatus status;

    private String sessionId;

    private String userLogin;

    private String ipAddress;

    private String page;

    private UUID storeGuid;

    private Instant time;

    private String device;

    private String deviceOS;

    private String browser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityDTO dto = (ActivityDTO) o;
        return sessionId.equals(dto.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }
}
