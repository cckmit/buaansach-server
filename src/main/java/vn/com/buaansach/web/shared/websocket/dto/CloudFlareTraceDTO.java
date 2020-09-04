package vn.com.buaansach.web.shared.websocket.dto;

import lombok.Data;

@Data
public class CloudFlareTraceDTO {
    private String ip;
    private String loc;
    private String uag;
    private String warp;
}
