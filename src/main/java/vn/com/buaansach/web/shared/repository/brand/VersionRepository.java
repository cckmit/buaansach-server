package vn.com.buaansach.web.shared.repository.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.brand.VersionEntity;
import vn.com.buaansach.entity.enumeration.VersionType;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VersionRepository extends JpaRepository<VersionEntity, Long> {

    @Query(value = "SELECT ve FROM bas_version ve WHERE ve.version_type = :versionType ORDER BY ve.id DESC LIMIT 1", nativeQuery = true)
    Optional<VersionEntity> findLatestVersionByType(@Param("versionType") VersionType versionType);

    Optional<VersionEntity> findOneByGuid(UUID fromString);
}
