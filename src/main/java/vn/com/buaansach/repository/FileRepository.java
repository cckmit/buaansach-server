package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.FileEntity;

import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findOneByCode(UUID code);

    FileEntity findOneByUrl(String fileUrl);
}
