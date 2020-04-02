package vn.com.buaansach.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.FileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.repository.FileRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileService {
    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;
    private final FileRepository fileRepository;
    @Value("${server.port}")
    private Long serverPort;
    @Value("${app.server-domain}")
    private String serverDomain;
    @Value("${app.upload-dir}")
    private String uploadDir;
    @Value("${app.seat-location-url}")
    private String seatLocationUrl;

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

    public InputStreamResource downloadFile(HttpServletResponse response, String guid) throws IOException {
        Optional<FileEntity> optional = fileRepository.findOneByGuid(UUID.fromString(guid));
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
        throw new BadRequestException("error.fileNotFound");
    }

    public void deleteByUrl(String fileUrl) {
        fileRepository.findOneByUrl(fileUrl).ifPresent(fileEntity -> {
            File file = new File(fileEntity.getLocalUrl());
            if (file.exists()) file.delete();
            fileRepository.delete(fileEntity);
        });
    }

    private String getExtensionFile(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    private Map<EncodeHintType, Object> getQRCodeConfig() {
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); /* default = 4 */
        return hints;
    }

    public FileEntity generateQRCodeForSeat(UUID guid) {
        String extension = ".png";
        String customPath = "seats";

        FileEntity fileEntity = new FileEntity();
        fileEntity.setGuid(guid);
        fileEntity.setOriginalName(guid + extension);
        fileEntity.setContentType("image/png");
        fileEntity.setExtension(extension);
        fileEntity.setSize(1);

        String fileName = guid + extension;

        /* URL that when user scan QR code */
        String content = seatLocationUrl + guid;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT, getQRCodeConfig());
            String localFilePath = uploadDir.substring(8) + customPath + "/" + fileName;
            Path path = FileSystems.getDefault().getPath(localFilePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            String clientSidePath;
            if (serverPort != 80) {
                clientSidePath = serverDomain + ":" + serverPort + "/storage/" + customPath;
            } else {
                clientSidePath = serverDomain + "/storage/" + customPath;
            }

            fileEntity.setLocalUrl(localFilePath);
            fileEntity.setUrl(clientSidePath + "/" + fileName);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return new FileEntity();
        }
        return fileRepository.save(fileEntity);
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
            return new FileEntity();
        }
        return fileRepository.save(fileEntity);
    }


}
