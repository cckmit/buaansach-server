package vn.com.buaansach.web.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.buaansach.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findOneByLogin(String login);

    Optional<UserEntity> findOneByPhone(String phone);

    Optional<UserEntity> findOneByEmailIgnoreCase(String email);

    Optional<UserEntity> findOneByResetKey(String resetKey);
}
