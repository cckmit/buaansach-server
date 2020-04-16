package vn.com.buaansach.web.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.FileEntity;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findOneByGuid(UUID guid);

    Optional<FileEntity> findOneByUrl(String fileUrl);
}
