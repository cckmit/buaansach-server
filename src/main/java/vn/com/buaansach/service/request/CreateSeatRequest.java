package vn.com.buaansach.service.request;

import lombok.Data;

@Data
public class CreateSeatRequest {
    private String areaGuid;
    private String seatName;
}
