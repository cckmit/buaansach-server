package vn.com.buaansach.web.general.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.general.repository.common.GeneralFileRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    private final Logger log = LoggerFactory.getLogger(FileService.class);
    private final GeneralFileRepository generalFileRepository;
    @Value("${server.port}")
    private Long serverPort;
    @Value("${app.server-domain}")
    private String serverDomain;
    @Value("${app.upload-dir}")
    private String uploadDir;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public FileService(GeneralFileRepository generalFileRepository) {
        this.generalFileRepository = generalFileRepository;
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

    public InputStreamResource downloadFile(HttpServletResponse response, String guid) throws IOException {
        Optional<FileEntity> optional = generalFileRepository.findOneByGuid(UUID.fromString(guid));
        if (optional.isPresent()) {
            FileEntity entity = optional.get();
            InputStream input;
            input = new URL(entity.getUrl()).openStream();
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode(entity.getOriginalName(), String.valueOf(StandardCharsets.UTF_8));
            fileName = fileName.replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            return new InputStreamResource(input);
        }
        throw new NotFoundException("File not found with guid: " + guid);
    }

    public void deleteByUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;
        generalFileRepository.findOneByUrl(fileUrl).ifPresent(fileEntity -> {
            File file = new File(fileEntity.getLocalUrl());
            if (file.exists()) file.delete();
            generalFileRepository.delete(fileEntity);
        });
    }

    private String getExtensionFile(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    /**
     * @param customPath: relative path to save file on server, concat with uploadDir in application.yml file;
     * @param guid:       guid will be filename on hard disk
     */
    private FileEntity uploadFile(MultipartFile file, String customPath, UUID guid) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setGuid(guid);
        fileEntity.setOriginalName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setSize(file.getSize());

        String extension = getExtensionFile(Objects.requireNonNull(file.getOriginalFilename()));
        fileEntity.setExtension(extension);

        // file name to be save on hard disk
        String fileName = guid + extension;

        try {
            String localDir = uploadDir + customPath;
            Files.createDirectories(Paths.get(localDir));
            Files.copy(file.getInputStream(), Paths.get(localDir, fileName));
            // path for client side
            String clientSidePath;
            if (activeProfile.equals(Constants.SPRING_PROFILE_DEVELOPMENT)) {
                clientSidePath = serverDomain + ":" + serverPort + "/storage/" + customPath;
            } else {
                clientSidePath = serverDomain + "/storage/" + customPath;
            }
            fileEntity.setLocalUrl(localDir + "/" + fileName);
            fileEntity.setUrl(clientSidePath + "/" + fileName);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new FileEntity();
        }
        return generalFileRepository.save(fileEntity);
    }


}
