package vn.com.buaansach.model.dto.manipulation;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateSeatDTO {
    private UUID areaGuid;
    private String seatName;
}
