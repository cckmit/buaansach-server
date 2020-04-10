package vn.com.buaansach.web.rest.guest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.service.dto.guest.GuestProductDTO;
import vn.com.buaansach.service.guest.GuestProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/guest/product")
public class GuestProductResource {
    private final GuestProductService guestProductService;

    public GuestProductResource(GuestProductService guestProductService) {
        this.guestProductService = guestProductService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<GuestProductDTO>> getListProduct() {
        return ResponseEntity.ok(guestProductService.getList().stream()
                .map(GuestProductDTO::new)
                .collect(Collectors.toList()));
    }
}
