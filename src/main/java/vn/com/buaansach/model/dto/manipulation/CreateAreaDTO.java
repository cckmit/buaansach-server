package vn.com.buaansach.model.dto.manipulation;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAreaDTO {
    private UUID storeGuid;
    private String areaName;
    private String seatPrefix;
    private int numberOfSeats;
}
