package vn.com.buaansach.web.shared.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.web.shared.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/public/file")
@RequiredArgsConstructor
public class FileResource {
    private final FileService fileService;

    @GetMapping("/download/{fileGuid}")
    public InputStreamResource downloadFile(@PathVariable String fileGuid, HttpServletResponse response) throws IOException {
        return fileService.downloadFile(response, fileGuid);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "fileName") String fileName, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(fileName, file).getUrl());
    }
}
