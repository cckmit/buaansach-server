package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.brand.VersionEntity;
import vn.com.buaansach.entity.enumeration.VersionType;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.web.admin.repository.brand.AdminVersionRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminVersionService {
    private final AdminVersionRepository adminVersionRepository;

    public VersionEntity createVersion(VersionEntity payload) {
        payload.setGuid(UUID.randomUUID());
        return adminVersionRepository.save(payload);
    }

    public void updateVersion(VersionEntity payload) {
        VersionEntity entity = adminVersionRepository.findOneByGuid(payload.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.VERSION_NOT_FOUND));
        entity.setVersionDeployed(!entity.isVersionDeployed());
        adminVersionRepository.save(entity);
    }

    public void deleteVersion(String versionGuid) {
        adminVersionRepository.findOneByGuid(UUID.fromString(versionGuid)).ifPresent(adminVersionRepository::delete);
    }

    public List<VersionEntity> getAllVersion() {
        return adminVersionRepository.findAll();
    }

    public VersionEntity getLatestVersion(String versionType) {
        List<VersionEntity> list = adminVersionRepository.findByVersionTypeAndVersionDeployedTrueOrderByIdDesc(VersionType.valueOf(versionType));
        return list.get(0);
    }
}
