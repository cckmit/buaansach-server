package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.guest.service.GuestVoucherService;
import vn.com.buaansach.web.guest.service.dto.read.GuestVoucherDTO;

@RestController
@RequestMapping("/api/v1/guest/voucher")
@RequiredArgsConstructor
public class GuestVoucherResource {
    private final String ENTITY_NAME = "guest-voucher";
    private final Logger log = LoggerFactory.getLogger(GuestStoreResource.class);
    private final GuestVoucherService guestVoucherService;

    @GetMapping("/get-first-register-voucher")
    public ResponseEntity<GuestVoucherDTO> getFirstRegisterVoucher() {
        log.debug("REST request from user [{}] to get fist register [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        return ResponseEntity.ok(guestVoucherService.getFirstRegisterVoucher());
    }
}
