package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.web.shared.service.SeatIdentityService;

@Service
@RequiredArgsConstructor
public class AdminSeatIdentityService {
    private final SeatIdentityService seatIdentityService;

    public void updateAllSeatIdentity() {
        seatIdentityService.createAllSeatIdentity();
    }
}
