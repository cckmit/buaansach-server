package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.brand.VersionEntity;
import vn.com.buaansach.entity.enumeration.VersionType;
import vn.com.buaansach.web.shared.repository.brand.VersionRepository;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;

    public VersionEntity getLatestVersion(String versionType) {
        return versionRepository.findLatestVersionByType(VersionType.valueOf(versionType))
                .orElse(new VersionEntity());
    }
}
