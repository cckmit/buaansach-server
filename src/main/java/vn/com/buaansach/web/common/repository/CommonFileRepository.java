package vn.com.buaansach.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.FileEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommonFileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findOneByGuid(UUID guid);

    Optional<FileEntity> findOneByUrl(String fileUrl);
}
