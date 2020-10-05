package vn.com.buaansach.web.shared.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.common.SequenceEntity;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface SequenceRepository extends JpaRepository<SequenceEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SequenceEntity> findOneBySequenceId(String sequenceId);
}
