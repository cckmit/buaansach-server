package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.model.entity.AuthorityEntity;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String> {
    Optional<AuthorityEntity> findByName(String name);
}
