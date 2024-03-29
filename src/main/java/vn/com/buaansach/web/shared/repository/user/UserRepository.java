package vn.com.buaansach.web.shared.repository.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.buaansach.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findOneByActivationKey(String activationKey);

    Optional<UserEntity> findOneByResetKey(String resetKey);

    Optional<UserEntity> findOneByUserLoginIgnoreCase(String login);

    Optional<UserEntity> findOneByUserEmailIgnoreCase(String email);

    Optional<UserEntity> findOneByUserPhone(String phone);

    Optional<UserEntity> findOneByUserLoginIgnoreCaseOrUserEmailIgnoreCaseOrUserPhone(String login, String email, String phone);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByUserLoginIgnoreCase(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByUserEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByUserPhone(String phone);

    Optional<UserEntity> findOneByGuid(UUID userGuid);

    List<UserEntity> findByUserLoginIgnoreCaseIn(List<String> listUserLogin);
}
