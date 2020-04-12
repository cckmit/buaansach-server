package vn.com.buaansach.web.common.service.dto.manipulation;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAreaDTO {
    /*used for admin and manager*/
    private UUID storeGuid;
    private String areaName;
    private String seatPrefix;
    private int numberOfSeats;
}
