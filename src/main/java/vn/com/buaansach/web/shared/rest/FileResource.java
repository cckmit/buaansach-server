package vn.com.buaansach.web.shared.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.web.shared.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileResource {
    private final FileService fileService;

    @GetMapping("/download/{fileGuid}")
    public InputStreamResource downloadFile(@PathVariable String fileGuid, HttpServletResponse response) throws IOException {
        return fileService.downloadFile(response, fileGuid);
    }
}
