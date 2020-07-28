package vn.com.buaansach.web.guest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.util.WebSocketConstants;
import vn.com.buaansach.web.guest.exception.GuestResourceNotFoundException;
import vn.com.buaansach.web.guest.repository.GuestStoreRepository;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreDTO;
import vn.com.buaansach.web.guest.websocket.GuestSocketService;
import vn.com.buaansach.web.guest.websocket.dto.GuestCallWaiterDTO;
import vn.com.buaansach.web.guest.websocket.dto.GuestSocketDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestStoreService {
    private final GuestStoreRepository guestStoreRepository;
    private final GuestSocketService guestSocketService;

    public GuestStoreDTO getStoreBySeat(String seatGuid) {
        return new GuestStoreDTO(guestStoreRepository.findOneBySeatGuid(UUID.fromString(seatGuid))
                .orElseThrow(() -> new GuestResourceNotFoundException("guest@storeNotFound")));
    }

    public void callWaiter(GuestCallWaiterDTO payload) {
        GuestSocketDTO socketDTO = new GuestSocketDTO(WebSocketConstants.GUEST_CALL_WAITER, payload);
        guestSocketService.sendMessage(WebSocketConstants.TOPIC_POS_PREFIX + payload.getStoreGuid(), socketDTO);
    }
}
