package vn.com.buaansach.web.guest.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.web.guest.service.GuestStoreProductService;
import vn.com.buaansach.web.guest.service.dto.read.GuestStoreProductDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guest/store-product")
@RequiredArgsConstructor
public class GuestStoreProductResource {
    private final String ENTITY_NAME = "guest-store-product";
    private final Logger log = LoggerFactory.getLogger(GuestStoreProductResource.class);
    private final GuestStoreProductService guestStoreProductService;

//    @GetMapping("/list-by-store/{storeGuid}")
//    public ResponseEntity<List<GuestStoreProductDTO>> getListStoreProduct(@PathVariable String storeGuid) {
//        return ResponseEntity.ok(guestStoreProductService.getListStoreProduct(storeGuid));
//    }
}
