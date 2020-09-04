package vn.com.buaansach.web.shared.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/public/server-time")
@RequiredArgsConstructor
public class ServerTimeResource {
    @GetMapping("/get")
    public ResponseEntity<Instant> getServerTime() {
        return ResponseEntity.ok(Instant.now());
    }
}
