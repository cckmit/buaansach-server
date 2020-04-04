package vn.com.buaansach.service.request;

import lombok.Data;

@Data
public class CreateAreaRequest {
    private String storeGuid;
    private String areaName;
    private String seatPrefix;
    private int numberOfSeats;
}
