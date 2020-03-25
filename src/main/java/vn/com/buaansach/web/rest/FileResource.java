package vn.com.buaansach.web.rest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileResource {
    private final FileService fileService;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download/{fileCode}")
    public InputStreamResource downloadFile(@PathVariable String fileCode, HttpServletResponse response) throws IOException {
        return fileService.downloadFile(response, fileCode);
    }
}
