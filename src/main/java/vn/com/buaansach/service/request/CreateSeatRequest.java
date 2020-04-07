package vn.com.buaansach.service.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateSeatRequest {
    private UUID areaGuid;
    private String seatName;
}
