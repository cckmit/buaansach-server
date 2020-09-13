package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.brand.BannerEntity;
import vn.com.buaansach.entity.common.FileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.web.admin.repository.brand.AdminBannerRepository;
import vn.com.buaansach.web.shared.service.FileService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminBannerService {
    private final AdminBannerRepository adminBannerRepository;
    private final FileService fileService;

    public BannerEntity createBanner(BannerEntity payload, MultipartFile image) {
        if (image == null) throw new BadRequestException(ErrorCode.BANNER_IMAGE_CANNOT_BE_NULL);
        FileEntity file = fileService.uploadImage(image, Constants.BANNER_IMAGE_PATH);
        payload.setGuid(UUID.randomUUID());
        payload.setBannerImageUrl(file.getUrl());
        payload.setBannerActivated(true);
        return adminBannerRepository.save(payload);
    }

    public BannerEntity updateBanner(BannerEntity payload, MultipartFile image) {
        BannerEntity entity = adminBannerRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BANNER_NOT_FOUND));
        payload.setId(entity.getId());
        if (image != null) {
            fileService.deleteByUrl(entity.getBannerImageUrl());
            FileEntity file = fileService.uploadImage(image, Constants.BANNER_IMAGE_PATH);
            payload.setBannerImageUrl(file.getUrl());
        }
        return adminBannerRepository.save(payload);
    }

    public List<BannerEntity> getListBanner() {
        return adminBannerRepository.findAll();
    }

    public void deleteBanner(String bannerGuid) {
        BannerEntity entity = adminBannerRepository.findOneByGuid(UUID.fromString(bannerGuid))
                .orElseThrow(() -> new NotFoundException(ErrorCode.BANNER_NOT_FOUND));
        fileService.deleteByUrl(entity.getBannerImageUrl());
        adminBannerRepository.delete(entity);
    }
}
