package vn.com.buaansach.model.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAreaRequest {
    private UUID storeGuid;
    private String areaName;
    private String seatPrefix;
    private int numberOfSeats;
}
