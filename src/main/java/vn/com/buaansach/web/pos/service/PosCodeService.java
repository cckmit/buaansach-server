package vn.com.buaansach.web.pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.zip.CRC32;

@Service
@RequiredArgsConstructor
public class PosCodeService {

    public String generateCodeForCustomer(String customerPhone, Instant createdDate) {
        String plainText = customerPhone + "@" + createdDate.toString();
        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(plainText.getBytes());
        return crc32.getValue() + "";
    }

}
