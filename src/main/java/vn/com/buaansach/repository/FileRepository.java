package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.FileEntity;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findOneByCode(UUID code);

    Optional<FileEntity> findOneByUrl(String fileUrl);
}
