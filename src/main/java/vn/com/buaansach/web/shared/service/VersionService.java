package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.brand.VersionEntity;
import vn.com.buaansach.entity.enumeration.VersionType;
import vn.com.buaansach.web.shared.repository.brand.VersionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;

    public String getLatestVersion(String versionType) {
        List<VersionEntity> list =  versionRepository.findByVersionTypeAndVersionDeployedTrueOrderByIdDesc(VersionType.valueOf(versionType));
        return list.size() > 0 ? list.get(0).getVersionNumber() : null;
    }
}
