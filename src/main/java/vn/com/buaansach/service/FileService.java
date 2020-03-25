package vn.com.buaansach.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.repository.FileRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;
    @Value("${server.port}")
    private Long serverPort;
    @Value("${app.server-domain}")
    private String serverDomain;
    @Value("${app.upload-dir}")
    private String uploadDir;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /* default image directory */
    public FileEntity uploadImage(MultipartFile image) {
        return uploadFile(image, "images", UUID.randomUUID());
    }

    /* custom image directory */
    public FileEntity uploadImage(MultipartFile image, String customPath) {
        return uploadFile(image, customPath, UUID.randomUUID());
    }

    /* default attachment directory */
    public FileEntity uploadAttachment(MultipartFile attachment) {
        return uploadFile(attachment, "attachments", UUID.randomUUID());
    }

    /* custom attachment directory */
    public FileEntity uploadAttachment(MultipartFile attachment, String customPath) {
        return uploadFile(attachment, customPath, UUID.randomUUID());
    }

    public InputStreamResource downloadFile(HttpServletResponse response, String code) throws IOException {
        FileEntity entity = fileRepository.findOneByCode(UUID.fromString(code));
        InputStream input;
        input = new URL(entity.getUrl()).openStream();
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode(entity.getOriginalName(), String.valueOf(StandardCharsets.UTF_8));
        fileName = fileName.replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return new InputStreamResource(input);
    }

    public void deleteByUrl(String fileUrl) {
        FileEntity entity = fileRepository.findOneByUrl(fileUrl);
//        File file = new File(entity.getLocalUrl());
//        if (file.exists()) file.delete();
        entity.setDeleted(true);
        fileRepository.save(entity);
    }

    private String getExtensionFile(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    /**
     * @param customPath: relative path to save file on server, concat with uploadDir in application.yml file;
     * @param code:       code will be filename on hard disk
     */
    private FileEntity uploadFile(MultipartFile file, String customPath, UUID code) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setCode(code);
        fileEntity.setOriginalName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileEntity.setDeleted(false);

        String extension = getExtensionFile(Objects.requireNonNull(file.getOriginalFilename()));
        fileEntity.setExtension(extension);

        // file name to be save on hard disk
        String fileName = code + extension;

        try {
            String localDir = uploadDir.substring(8) + customPath;
            Files.createDirectories(Paths.get(localDir));
            Files.copy(file.getInputStream(), Paths.get(localDir, fileName));
            // path for client side
            String clientSidePath;
            if (serverPort != 80) {
                clientSidePath = serverDomain + ":" + serverPort + "/storage/" + customPath;
            } else {
                clientSidePath = serverDomain + "/storage/" + customPath;
            }
            fileEntity.setLocalUrl(localDir + "/" + fileName);
            fileEntity.setUrl(clientSidePath + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileRepository.save(fileEntity);
    }


}
