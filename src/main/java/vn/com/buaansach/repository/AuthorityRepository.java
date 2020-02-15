package vn.com.buaansach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.Authority;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Optional<Authority> findByName(String name);
}
